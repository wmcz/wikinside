package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {}
