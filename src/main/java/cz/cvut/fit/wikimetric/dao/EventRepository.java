package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Event;
import cz.cvut.fit.wikimetric.model.EventTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface EventRepository extends CrudRepository<Event, Long> {
    Collection<Event> findEventsByName(String name);
    @Query(value = "select distinct e from Event e where ?1 in e.tags")
    Collection<Event> findEventsByTag(EventTag tag);


}
