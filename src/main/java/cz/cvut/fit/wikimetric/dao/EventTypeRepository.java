package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.EventType;
import org.springframework.data.repository.CrudRepository;

public interface EventTypeRepository extends CrudRepository<EventType, Long> {
}
