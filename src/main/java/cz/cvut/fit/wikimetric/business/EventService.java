package cz.cvut.fit.wikimetric.business;

import cz.cvut.fit.wikimetric.model.Event;
import cz.cvut.fit.wikimetric.model.EventTag;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public class EventService extends AbstractService<Event, Long> {

    public EventService(CrudRepository<Event, Long> repository) {
        super(repository);
    }

    public Optional<Event> addTag(Event event, EventTag tag) {
        return update(event.addTag(tag));
    }

    public Optional<Event> removeTag(Event event, EventTag tag) {
        return update(event.removeTag(tag));
    }
}
