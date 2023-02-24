package cz.cvut.fit.wikimetric.business;


import cz.cvut.fit.wikimetric.dao.EventRepository;
import cz.cvut.fit.wikimetric.dao.EventTagRepository;
import cz.cvut.fit.wikimetric.model.Event;
import cz.cvut.fit.wikimetric.model.EventTag;
import cz.cvut.fit.wikimetric.model.IdAble;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Service
public class EventTagService extends AbstractService<EventTag, Long> {

    private final EventRepository eventRepository;
    private final EventTagRepository eventTagRepository;
    public EventTagService(EventTagRepository repository, EventRepository eventRepository) {
        super(repository);
        this.eventRepository = eventRepository;
        this.eventTagRepository = repository;
    }

    private <T extends IdAble<Long>> Collection<T> updateNonOwningField(EventTag tag,
                                                                        Collection<Long> ids,
                                                                        Function<T, T> updatingFunc,
                                                                        CrudRepository<T, Long> repository,
                                                                        Function<EventTag, Collection<T>> fieldAccessFunc) {
        return updateNonOwningField(tag, ids, updatingFunc, repository, eventTagRepository, fieldAccessFunc);
    }

    private Collection<Event> updateEvents(EventTag tag, Collection<Long> eventIds, Function<Event, Event> function) {
        return updateNonOwningField(tag, eventIds, function, eventRepository, EventTag::getTagged);
    }

    private Collection<EventTag> updateChildren(EventTag tag, Collection<Long> eventIds, EventTag newParent) {
        return updateNonOwningField(tag, eventIds, c -> c.setParent(newParent), eventTagRepository, EventTag::getChildren);
    }

    public Optional<EventTag> setParent(EventTag tag, EventTag parent) {
        Optional<EventTag> res = update(tag.setParent(parent));
        if (update(parent).isEmpty())
            throw new IllegalStateException("Inconsistent data: parent-child relationship could not be established properly for the following tags:" + parent.getId() + ", " + tag.getId());
        return res;
    }

    public Collection<Event> getEventsWithTag(EventTag tag) {
        Collection<Event> result = tag.getTagged();
        tag.getChildren().forEach(child -> result.addAll(getEventsWithTag(child)));
        tag.getChildren().forEach(child -> result.addAll(getEventsWithTag(child)));
        return result;
    }

    public Collection<EventTag> findByName(String name) {
        return eventTagRepository.findEventTagsByName(name);
    }

    public Collection<Event> addEvents(EventTag tag, Collection<Long> eventIds) {
        return updateEvents(tag, eventIds, u -> u.addTag(tag));
    }

    public Collection<Event> removeEvents(EventTag tag, Collection<Long> eventIds) {
        return updateEvents(tag, eventIds, u -> u.removeTag(tag));
    }

    public Collection<EventTag> addChildren(EventTag tag, Collection<Long> childrenIds) {
        return updateChildren(tag, childrenIds, tag);
    }

    public Collection<EventTag> removeChildren(EventTag tag, Collection<Long> childrenIds) {
        return updateChildren(tag, childrenIds, null);
    }
}
