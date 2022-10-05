package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.entity.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {
}
