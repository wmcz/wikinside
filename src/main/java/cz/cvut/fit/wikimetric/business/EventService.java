package cz.cvut.fit.wikimetric.business;

import cz.cvut.fit.wikimetric.dao.EventRepository;
import cz.cvut.fit.wikimetric.model.Event;
import cz.cvut.fit.wikimetric.model.EventTag;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class EventService extends AbstractService<Event, Long> {
    private final EventRepository eventRepository;

    public EventService(EventRepository repository) {
        super(repository);
        this.eventRepository = repository;
    }

    public Optional<Event> addTag(Event event, EventTag tag) {
        return update(event.addTag(tag));
    }

    public Optional<Event> removeTag(Event event, EventTag tag) {
        return update(event.removeTag(tag));
    }

    public Collection<Event> findByName(String name) {
        return eventRepository.findEventsByName(name);
    }
}
