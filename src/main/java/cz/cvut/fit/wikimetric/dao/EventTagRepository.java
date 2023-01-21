package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.EventTag;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface EventTagRepository extends CrudRepository<EventTag, Long> {
    Collection<EventTag> findEventTagsByName(String name);
}
