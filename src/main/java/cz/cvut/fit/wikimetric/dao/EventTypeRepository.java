package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.EventType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EventTypeRepository extends CrudRepository<EventType, Long> {
    Optional<EventType> findEventTypeByName(String name);
}
