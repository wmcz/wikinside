package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface EventRepository extends CrudRepository<Event, Long> {
    Collection<Event> findEventsByName(String name);
}
