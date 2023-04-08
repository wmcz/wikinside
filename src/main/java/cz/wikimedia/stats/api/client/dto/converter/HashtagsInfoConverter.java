package cz.wikimedia.stats.api.client.dto.converter;

import cz.wikimedia.stats.api.client.dto.HashtagsInfo;
import cz.wikimedia.stats.api.client.dto.WmPage;
import cz.wikimedia.stats.api.client.dto.WmRev;
import cz.wikimedia.stats.business.external.WmPageService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HashtagsInfoConverter {

    private final UserService userService;
    private final WmPageService wmPageService;

    public HashtagsInfoConverter(UserService userService, WmPageService wmPageService) {
        this.userService = userService;
        this.wmPageService = wmPageService;
    }

    public Revision fromOneInfo(HashtagsInfo info, Project project, Event event, Map<String, WmPage> pages) {
        WmPage page = pages.get(info.pageTitle());
        Long firstRevId = page.revs().stream().findFirst().map(WmRev::revId).orElse(null);
        return new Revision(
                null,
                info.revId(),
                null,
                page.pageId(),
                info.revId().equals(firstRevId),
                userService.processUser(info.username()),
                Collections.singleton(event),
                project,
                info.timestamp(),
                info.summary());
    }

    public Collection<Revision> fromInfo(Collection<HashtagsInfo> info, Project project, Event event) {
       Map<String, WmPage> pages = wmPageService
               .getPages(info.stream().map(HashtagsInfo::pageTitle).toList(), project)
               .stream()
               .collect(Collectors.toMap(WmPage::title, p -> p));
       return info.stream().map(i -> fromOneInfo(i, project, event, pages)).toList();
    }
}
