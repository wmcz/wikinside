package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.EventTag;
import org.springframework.data.repository.CrudRepository;

public interface EventTagRepository extends CrudRepository<EventTag, Long> {}
