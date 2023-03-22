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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
public class WmRevisionService {

    private final WmClientService wmClientService;
    private final UserContribConverter userContribConverter;

    public WmRevisionService(WmClientService wmClientService, UserContribConverter userContribConverter) {
        this.wmClientService = wmClientService;
        this.userContribConverter = userContribConverter;
    }

    public Collection<Revision> getUserContribs(Collection<String> usernames, LocalDate start, LocalDate end, Project project) {
        WmClient client = wmClientService.getClient(project);
        String names = ClientUtils.collectNames(usernames);
        Instant startTime = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endTime = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        var response = client.getUserContribs(names, startTime, endTime);
        Collection<UserContrib> contribs = new ArrayList<>(response.query().contents());

        while (response.toContinue() != null) {
            response = client.getMoreUserContribs(names, startTime, endTime, response.toContinue().listContinue());
            contribs.addAll(response.query().contents());
        }
        return userContribConverter.fromContrib(contribs, project);
    }

    public Collection<Revision> getUserContribs(Collection<String> usernames, LocalDate start, LocalDate end, Collection<Project> projects) {
        Collection<Revision> res = new HashSet<>();
        projects.forEach(p -> res.addAll(getUserContribs(usernames, start, end, p)));
        return res;
    }

    public Collection<Revision> getUserContribs(Event event) {
        return getUserContribs(
                event.getParticipants().stream().map(User::getUsername).toList(),
                event.getStartDate(),
                event.getEndDate(),
                event.getProjects());
    }

}
