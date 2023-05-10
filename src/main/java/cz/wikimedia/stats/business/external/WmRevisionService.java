package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import cz.wikimedia.stats.api.client.dto.UserContrib;
import cz.wikimedia.stats.api.client.dto.WmRev;
import cz.wikimedia.stats.api.client.dto.converter.UserContribConverter;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WmRevisionService {

    private final WmClientService wmClientService;
    private final UserContribConverter userContribConverter;

    public WmRevisionService(WmClientService wmClientService, UserContribConverter userContribConverter) {
        this.wmClientService = wmClientService;
        this.userContribConverter = userContribConverter;
    }

    private Collection<UserContrib> getUserContribsInner(String names, Instant start, Instant end, WmClient client) {
        var response = client.getUserContribs(names, start, end);
        Collection<UserContrib> contribs = new ArrayList<>(response.query().contents());

        while (response.toContinue() != null) {
            response = client.getMoreUserContribs(names, start, end, response.toContinue().listContinue());
            contribs.addAll(response.query().contents());
        }

        return contribs;
    }

    private Collection<WmRev> getRevInfo(Collection<Long> ids, WmClient client) {
        return client
                .getRevInfoWithSizes(ClientUtils.collect(ids))
                .query()
                .contents()
                .stream()
                .flatMap(p -> p.revs().stream()).toList();
    }

    public Collection<Revision> getUserContribs(Collection<String> usernames, Instant start, Instant end, Project project) {
        WmClient client = wmClientService.getClient(project);

        return userContribConverter.fromContrib(
                ClientUtils.applyWithLimit(usernames, names -> getUserContribsInner(ClientUtils.collectNames(names), start, end, client)),
                project);
    }

    public Collection<Revision> getUserContribs(Collection<String> usernames, Instant start, Instant end, Collection<Project> projects) {
        Collection<Revision> res = new HashSet<>();
        projects.forEach(p -> res.addAll(getUserContribs(usernames, start, end, p)));
        return res;
    }

    public Collection<Revision> getUserContribs(Event event, Instant sinceWhen) {
        return getUserContribs(
                event.getParticipants().stream().map(User::getUsername).toList(),
                sinceWhen,
                event.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                event.getProjects());
    }

    public Collection<Revision> addDiffs(Collection<Revision> revs, Project project) {
        WmClient client = wmClientService.getClient(project);

        Collection<Long> revIds = revs.stream().reduce(
                new HashSet<>(revs.size() * 2),
                (col, rev) -> {col.add(rev.getRevId()); col.add(rev.getParentId()); return col;},
                (col, col2) -> {col.addAll(col2); return col;});

        Map<Long, Long> revSizes = ClientUtils.applyWithLimit(
                revIds,
                ids -> getRevInfo(ids, client))
               .stream()
               .collect(Collectors.toMap(WmRev::revId, WmRev::size));
        revSizes.put(0L, 0L);

        revs.forEach(r -> r.setDiff(revSizes.get(r.getRevId()) - revSizes.get(r.getParentId())));
        return revs;
    }
}
