package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.ImageRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Image;
import org.springframework.stereotype.Service;

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
        return applyEvents(image, eventIds, e -> e.removeImage(image));
    }

    private void updateEvents(Image image, Collection<Event> events) {
        updateNonOwningField(image, events, Image::getEvents, this::addEvents, this::removeEvents);
    }

    private void updateOrCreate(Image image) {
        Optional<Image> original = imageRepository.findById(image.getId());

        if (original.isPresent()) {
            Image og = original.get();
            og.addCategories(image.getCategories());

            updateEvents(og, image.getEvents());
            super.update(og);

        } else create(image);
    }

    public void updateOrCreate(Collection<Image> images) {
        images.forEach(this::updateOrCreate);
    }

    @Override
    public <S extends Image> Optional<S> create(S image) {
        Optional<S> res = super.create(image);
        if (res.isPresent()) {
            addEvents(res.get(), toIds(image.getEvents()));
            return update(res.get());
        }
        else return res;
    }

    @Override
    public <S extends Image> Optional<S> update(S image) {
        Optional<Image> current = findById(image.getId());
        if (current.isEmpty()) return Optional.empty();
        else {
            if (image.getEvents().isEmpty()) {
                deleteById(image.getId());
                return Optional.of(image);
            }
            updateEvents(current.get(), image.getEvents());
            return super.update(image);
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(r -> removeEvents(r, toIds(r.getEvents())));
        super.deleteById(id);

    }
}
