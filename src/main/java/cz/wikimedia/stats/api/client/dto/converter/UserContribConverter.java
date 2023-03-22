package cz.wikimedia.stats.api.client.dto.converter;

import cz.wikimedia.stats.api.client.dto.UserContrib;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class UserContribConverter {
    private final UserService userService;

    public UserContribConverter(UserService userService) {
        this.userService = userService;
    }

    public Revision fromContrib(UserContrib contrib, Project project) {
        return new Revision(
                null,
                contrib.revId(),
                contrib.sizeDiff(),
                userService.findByUsername(contrib.user()).orElse(null),
                null,
                project,
                contrib.timestamp(),
                contrib.comment());
    }

    public Revision fromContrib(UserContrib contrib, User user, Project project) {
        return new Revision(
                null,
                contrib.revId(),
                contrib.sizeDiff(),
                user,
                null,
                project,
                contrib.timestamp(),
                contrib.comment());
    }

    public Collection<Revision> fromContrib(Collection<UserContrib> contribs, Project project) {
        Collection<Revision> res = new HashSet<>();
        Map<Long, User> users = new HashMap<>();

        for (UserContrib contrib : contribs) {
            User user = users.get(contrib.userId());
            if (user == null) {
                user = userService.findByUsername(contrib.user()).get();
                users.put(contrib.userId(), user);
            }
            res.add(fromContrib(contrib, user, project));
        }

        return res;
    }
}
