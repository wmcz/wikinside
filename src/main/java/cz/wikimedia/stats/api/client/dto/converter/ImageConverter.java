package cz.wikimedia.stats.api.client.dto.converter;

import cz.wikimedia.stats.api.client.dto.GlobalUsage;
import cz.wikimedia.stats.api.client.dto.WmImage;
import cz.wikimedia.stats.api.client.dto.WmPage;
import cz.wikimedia.stats.business.internal.ImageCategoryService;
import cz.wikimedia.stats.business.internal.UsageService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ImageConverter {
    private final UserService userService;
    private final UsageService usageService;
    private final ImageCategoryService imageCategoryService;

    public ImageConverter(UserService userService, UsageService usageService, ImageCategoryService imageCategoryService) {
        this.userService = userService;
        this.usageService = usageService;
        this.imageCategoryService = imageCategoryService;
    }

    private Set<GlobalPage> getUsage(Collection<WmPage> commonsUsage, Collection<GlobalUsage> globalUsage) {
        Set<GlobalPage> pages = new HashSet<>();
        if (commonsUsage != null) commonsUsage.forEach(p -> pages.add(usageService.processUsage("commons.wikimedia.org", p.title())));
        if (globalUsage != null) globalUsage.forEach(p -> pages.add(usageService.processUsage(p.project(), p.title())));
        return pages;
    }

    public Image fromWmImage(WmImage image, Event event) {
        return new Image(
                image.pageId(),
                userService.processUser(image.imageInfo().stream().findAny().get().username()),
                new HashSet<>(Collections.singleton(event)),
                image.imageInfo().stream().findAny().get().timestamp(),
                image.title(),
                getUsage(image.commonsUsage(), image.globalUsage()),
                Set.of(imageCategoryService.processCategory(event.getCategory()))
        );
    }
}
