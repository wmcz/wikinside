package cz.wikimedia.stats.api.client.dto.converter;

import cz.wikimedia.stats.api.client.dto.HashtagsInfo;
import cz.wikimedia.stats.api.client.dto.WmPage;
import cz.wikimedia.stats.business.external.WmPageService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HashtagsInfoConverter {

    private final UserService userService;
    private final WmPageService wmPageService;

    public HashtagsInfoConverter(UserService userService, WmPageService wmPageService) {
        this.userService = userService;
        this.wmPageService = wmPageService;
    }

    private Revision fromOneInfo(HashtagsInfo info, Project project, Event event, Map<Long, WmPage> pages) {
        WmPage page = pages.get(info.revId());

        if (page == null) return null;
        else return new Revision(
                    null,
                    info.revId(),
                    null,
                    page.pageId(),
                    page.revs().iterator().next().parentId(),
                    userService.processUser(info.username()),
                    new HashSet<>(Collections.singleton(event)),
                    project,
                    info.timestamp(),
                    info.summary());
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

       return info.stream().map(i -> fromOneInfo(i, project, event, pages)).filter(Objects::nonNull).toList();
    }
}
