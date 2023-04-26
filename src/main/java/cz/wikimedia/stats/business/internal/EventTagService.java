package cz.wikimedia.stats.business.internal;


import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.EventTagRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.EventTag;
import cz.wikimedia.stats.model.IdAble;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Service
public class EventTagService extends InternalService<EventTag, Long> {

    private final EventRepository eventRepository;
    private final EventTagRepository eventTagRepository;
    public EventTagService(EventTagRepository repository, EventRepository eventRepository) {
        super(repository);
        this.eventRepository = eventRepository;
        this.eventTagRepository = repository;
    }

    private <T extends IdAble<Long>> Collection<T> applyToNonOwningField(EventTag tag,
                                                                         Collection<Long> ids,
                                                                         Function<T, T> updatingFunc,
                                                                         CrudRepository<T, Long> repository,
                                                                         Function<EventTag, Collection<T>> fieldAccessFunc) {
        return applyToNonOwningField(tag, ids, updatingFunc, repository, eventTagRepository, fieldAccessFunc);
    }

    private Collection<Event> applyEvents(EventTag tag, Collection<Long> eventIds, Function<Event, Event> function) {
        return applyToNonOwningField(tag, eventIds, function, eventRepository, EventTag::getTagged);
    }

    private Collection<EventTag> applyChildren(EventTag tag, Collection<Long> eventIds, EventTag newParent) {
        return applyToNonOwningField(tag, eventIds, c -> c.setParent(newParent), eventTagRepository, EventTag::getChildren);
    }

    private void updateEvents(EventTag tag, Collection<Event> updated) {
        updateNonOwningField(tag, updated, EventTag::getTagged, this::addEvents, this::removeEvents);
    }

    private void updateChildren(EventTag tag, Collection<EventTag> updated) {
        updateNonOwningField(tag, updated, EventTag::getChildren, this::addChildren, this::removeChildren);
    }

    public Collection<Event> addEvents(EventTag tag, Collection<Long> eventIds) {
        return applyEvents(tag, eventIds, u -> u.addTag(tag));
    }

    public Collection<Event> removeEvents(EventTag tag, Collection<Long> eventIds) {
        return applyEvents(tag, eventIds, u -> u.removeTag(tag));
    }

    public Collection<EventTag> addChildren(EventTag tag, Collection<Long> childrenIds) {
        return applyChildren(tag, childrenIds, tag);
    }

    public Collection<EventTag> removeChildren(EventTag tag, Collection<Long> childrenIds) {
        return applyChildren(tag, childrenIds, null);
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(t -> {
            removeEvents  (t, t.getTagged()  .stream().map(Event::getId)   .toList());
            removeChildren(t, t.getChildren().stream().map(EventTag::getId).toList());
        });
        super.deleteById(id);
    }

    @Override
    public <S extends EventTag> Optional<S> create(S tag) {
        Optional<S> res = super.create(tag);
        if (res.isPresent()) {
            addEvents   (res.get(), toIds(tag.getTagged()));
            addChildren(res.get(), toIds(tag.getChildren()));
            return update(res.get());

        } else return res;
    }

    @Override
    public <S extends EventTag> Optional<S> update(S tag) {
        Optional<EventTag> current = findById(tag.getId());

        if (current.isEmpty()) return Optional.empty();
        else {
            updateEvents   (current.get(), tag.getTagged());
            updateChildren(current.get(), tag.getChildren());
            return super.update(tag);
        }
    }
}
