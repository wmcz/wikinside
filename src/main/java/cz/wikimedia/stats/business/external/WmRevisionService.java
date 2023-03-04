package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import cz.wikimedia.stats.api.client.dto.UserContrib;
import cz.wikimedia.stats.api.client.dto.converter.UserContribConverter;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

@Service
public class WmRevisionService {

    private final WmClientService wmClientService;
    private final UserContribConverter userContribConverter;

    public WmRevisionService(WmClientService wmClientService, UserContribConverter userContribConverter) {
        this.wmClientService = wmClientService;
        this.userContribConverter = userContribConverter;
    }

    private String collectNames(Collection<String> usernames) {
        if (usernames.isEmpty()) return "";

        Iterator<String> iter = usernames.iterator();
        StringBuilder res = new StringBuilder(iter.next());
        while (iter.hasNext())
            res.append('|')
               .append(iter.next());
        return res.toString();
    }

    public Collection<Revision> getUserContribs(Collection<String> usernames, Instant start, Instant end, Project project) {
        WmClient client = wmClientService.getClient(project);
        String names = collectNames(usernames);

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
}
