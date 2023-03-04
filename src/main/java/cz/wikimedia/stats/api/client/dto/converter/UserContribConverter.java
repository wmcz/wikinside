package cz.wikimedia.stats.api.client.dto.converter;

import cz.wikimedia.stats.api.client.dto.UserContrib;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Component;

import java.util.Collection;

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
                project);
    }

    public Collection<Revision> fromContrib(Collection<UserContrib> contribs, Project project) {
        return contribs.stream().map(c -> fromContrib(c, project)).toList();
    }
}
