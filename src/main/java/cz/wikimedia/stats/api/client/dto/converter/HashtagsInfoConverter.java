package cz.wikimedia.stats.api.client.dto.converter;

import cz.wikimedia.stats.api.client.dto.HashtagsInfo;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Component;

@Component
public class HashtagsInfoConverter {

    private final UserService userService;

    public HashtagsInfoConverter(UserService userService) {
        this.userService = userService;
    }

    public Revision fromInfo(HashtagsInfo info, Project project, Event event) {
        return new Revision(
                null,
                info.revId(),
                null,
                userService.processUser(info.username()),
                event,
                project,
                info.timestamp(),
                info.summary());
    }
}
