package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.ImageRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Image;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ImageService extends InternalService<Image, Long> {
    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;

    public ImageService(ImageRepository repository, ImageRepository imageRepository, EventRepository eventRepository) {
        super(repository);
        this.imageRepository = imageRepository;
        this.eventRepository = eventRepository;
    }

    private Collection<Event> applyEvents(Image image, Collection<Long> eventIds, Function<Event, Event> func) {
        return applyToNonOwningField(image, eventIds, func, eventRepository, imageRepository, Image::getEvents);
    }

    private Collection<Event> addEvents(Image image, Collection<Long> eventIds) {
        return applyEvents(image, eventIds, e -> e.addImage(image));
    }

    private Collection<Event> removeEvents(Image image, Collection<Long> eventIds) {
        return applyEvents(image, eventIds, e -> e.addImage(image));
    }

    private void updateEvents(Image image, Collection<Event> events) {
        updateNonOwningField(image, events, Image::getEvents, this::addEvents, this::removeEvents);
    }

    private void updateOrCreate(Image image) {
        Optional<Image> original = imageRepository.findById(image.getId());

        if (original.isPresent()) {
            Collection<Event> events = new ArrayList<>(original.get().getEvents());
            events.addAll(image.getEvents());
            updateEvents(original.get(), events);
        }
    }

    public void updateOrCreate(Collection<Image> images) {
        images.forEach(this::updateOrCreate);
    }
}
