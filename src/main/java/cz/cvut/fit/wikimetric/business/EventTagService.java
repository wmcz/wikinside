package cz.cvut.fit.wikimetric.business;


import cz.cvut.fit.wikimetric.dao.EventRepository;
import cz.cvut.fit.wikimetric.dao.EventTagRepository;
import cz.cvut.fit.wikimetric.model.Event;
import cz.cvut.fit.wikimetric.model.EventTag;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class EventTagService extends AbstractService<EventTag, Long> {

    private final EventRepository eventRepository;
    private final EventTagRepository eventTagRepository;
    public EventTagService(EventTagRepository repository, EventRepository eventRepository) {
        super(repository);
        this.eventRepository = eventRepository;
        this.eventTagRepository = repository;
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
        return result;
    }

    public Collection<EventTag> findByName(String name) {
        return eventTagRepository.findEventTagsByName(name);
    }
}
