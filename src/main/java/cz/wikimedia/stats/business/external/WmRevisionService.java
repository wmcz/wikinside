package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import cz.wikimedia.stats.api.client.dto.UserContrib;
import cz.wikimedia.stats.api.client.dto.converter.UserContribConverter;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Service
public class WmRevisionService {

    private final WmClientService wmClientService;
    private final UserContribConverter userContribConverter;

    public WmRevisionService(WmClientService wmClientService, UserContribConverter userContribConverter) {
        this.wmClientService = wmClientService;
        this.userContribConverter = userContribConverter;
    }

    public Collection<Revision> getUserContribs(Collection<String> usernames, Instant start, Instant end, Project project) {
        WmClient client = wmClientService.getClient(project);
        String names = ClientUtils.collectNames(usernames);

        var response = client.getUserContribs(names, start, end);
        Collection<UserContrib> contribs = new ArrayList<>(response.query().contents());

        while (response.toContinue() != null) {
            response = client.getMoreUserContribs(names, start, end, response.toContinue().listContinue());
            contribs.addAll(response.query().contents());
        }
        return userContribConverter.fromContrib(contribs, project);
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
        Collection<Revision> queryrevs = new ArrayList<>(revs);
        Map<Long, Long> revSizes = new HashMap<>(revs.size());
        revSizes.put(0L, 0L);

        while (queryrevs.size() > 0) {
            Collection<Long> ids = new ArrayList<>(50);
            queryrevs.stream().limit(25).forEach(r -> {ids.add(r.getRevId()); ids.add(r.getParentId());});
            client
                    .getPageInfoWithSizes(ClientUtils.collect(ids))
                    .query()
                    .contents()
                    .forEach(p -> p.revs().forEach(r -> revSizes.put(r.revId(), r.size())));
            queryrevs = queryrevs.stream().skip(25).toList();
        }
        revs.forEach(r -> r.setDiff(revSizes.get(r.getRevId()) - revSizes.get(r.getParentId())));
        return revs;
    }
}
