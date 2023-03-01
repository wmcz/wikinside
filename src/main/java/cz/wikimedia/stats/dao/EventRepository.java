package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface EventRepository extends CrudRepository<Event, Long> {
    Collection<Event> findEventsByName(String name);
}
