package cz.wikimedia.stats.api.client.dto.converter;

import cz.wikimedia.stats.api.client.dto.HashtagsInfo;
import cz.wikimedia.stats.api.client.dto.WmPage;
import cz.wikimedia.stats.business.external.WmPageService;
import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HashtagsInfoConverter {

    private final UserService userService;
    private final WmPageService wmPageService;
    private final EventService eventService;

    public HashtagsInfoConverter(UserService userService, WmPageService wmPageService, EventService eventService) {
        this.userService = userService;
        this.wmPageService = wmPageService;
        this.eventService = eventService;
    }

    private Revision fromOneInfo(HashtagsInfo info, Project project, Event event, Map<Long, WmPage> pages) {
        WmPage page = pages.get(info.revId());
        User author = userService.processUser(info.username());

        if (page == null) return null;
        else {
            event.addParticipant(author);
            return new Revision(
                    null,
                    info.revId(),
                    null,
                    page.pageId(),
                    page.revs().iterator().next().parentId(),
                    author,
                    Collections.singleton(event),
                    project,
                    info.timestamp(),
                    info.summary());
        }
    }

    public Collection<Revision> fromInfo(Collection<HashtagsInfo> info, Project project, Event event) {
       Map<Long, WmPage> pages = new HashMap<>(info.size());
       wmPageService
               .getPages(info.stream().map(HashtagsInfo::revId).toList(), project)
               .forEach(page -> page.revs().forEach(rev -> pages.put(rev.revId(),
                       new WmPage(page.pageId(),
                                  page.namespace(),
                                  page.title(),
                                  Collections.singleton(rev)))));

       Collection<Revision> res = info.stream().map(i -> fromOneInfo(i, project, event, pages)).filter(Objects::nonNull).toList();
       eventService.update(event);
       return res;
    }
}
