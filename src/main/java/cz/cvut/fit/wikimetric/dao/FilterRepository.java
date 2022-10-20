package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Filter;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface FilterRepository extends CrudRepository<Filter, Long> {
    Optional<Filter> findFilterByName(String name);
}
