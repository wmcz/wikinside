package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.CommonsClient;
import cz.wikimedia.stats.api.client.dto.WmImage;
import cz.wikimedia.stats.api.client.dto.converter.ImageConverter;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Image;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Collection;

@Service
public class WmCommonsService {
    private final CommonsClient client;
    private final ImageConverter imageConverter;

    public WmCommonsService(CommonsClient client, ImageConverter imageConverter) {
        this.client = client;
        this.imageConverter = imageConverter;
    }


    public Collection<Image> getImages(Event event) {
        var response = client.getCategoryImageInfo(
                event.getCategory(),
                event.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant(),
                event.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Collection<WmImage> images = response.query().contents();

        while (response.toContinue() != null && response.toContinue().gcmContinue() != null) {
            response = client.getMoreCategoryImageInfo(
                    event.getCategory(),
                    event.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant(),
                    event.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                    response.toContinue().listContinue());
            images.addAll(response.query().contents());
        }

        return images.stream().filter(i -> i.imageInfo() != null).map(i -> imageConverter.fromWmImage(i, event)).toList();

    }
}
