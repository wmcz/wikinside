package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.entity.EventType;
import org.springframework.data.repository.CrudRepository;

public interface EventTypeRepository extends CrudRepository<EventType, Long> {
}
