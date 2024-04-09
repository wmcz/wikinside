package cz.wikimedia.stats.api.client.dto.converter;

import cz.wikimedia.stats.api.client.dto.GlobalUsage;
import cz.wikimedia.stats.api.client.dto.WmImage;
import cz.wikimedia.stats.api.client.dto.WmPage;
import cz.wikimedia.stats.business.internal.ImageService;
//import cz.wikimedia.stats.business.internal.UsageService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.GlobalPage;
import cz.wikimedia.stats.model.Image;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ImageConverter {
    private final ImageService imageService;
    private final UserService userService;

    public ImageConverter(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    private Set<GlobalPage> getUsage(Collection<WmPage> commonsUsage, Collection<GlobalUsage> globalUsage) {
        Set<GlobalPage> pages = new HashSet<>();commonsUsage.forEach(p -> pages.add(new GlobalPage("commons.wikimedia.org", p.title())));
        globalUsage.forEach(p -> pages.add(new GlobalPage(p.project(), p.title())));
        return pages;
    }

    public Image fromWmImage(WmImage image, Event event) {
        return new Image(
                image.pageId(),
                userService.processUser(image.imageInfo().username()),
                new HashSet<>(Collections.singleton(event)),
                image.imageInfo().timestamp(),
                image.title(),
                getUsage(image.commonsUsage(), image.globalUsage())
        );
    }
}
