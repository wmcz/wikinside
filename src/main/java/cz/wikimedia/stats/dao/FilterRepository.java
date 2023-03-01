package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Filter;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FilterRepository extends CrudRepository<Filter, Long> {
    Optional<Filter> findFilterByName(String name);
}
