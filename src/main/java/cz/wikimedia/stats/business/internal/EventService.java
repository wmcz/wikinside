package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.model.Event;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EventService extends InternalService<Event, Long> {
    private final EventRepository eventRepository;

    public EventService(EventRepository repository) {
        super(repository);
        this.eventRepository = repository;
    }

    public Collection<Event> findByName(String name) {
        return eventRepository.findEventsByName(name);
    }
}
