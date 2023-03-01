package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.EventTag;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface EventTagRepository extends CrudRepository<EventTag, Long> {
    Collection<EventTag> findEventTagsByName(String name);
}
