package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.CommonsClient;
import cz.wikimedia.stats.api.client.dto.converter.ImageConverter;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Image;
import org.springframework.stereotype.Service;

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
        return client
                .getCategoryImageInfo(event.getCategory())
                .query()
                .contents()
                .stream()
                .map(i -> imageConverter.fromWmImage(i, event))
                .toList();

    }
}
