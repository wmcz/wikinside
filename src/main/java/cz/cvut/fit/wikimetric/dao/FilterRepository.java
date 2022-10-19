package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Filter;
import org.springframework.data.repository.CrudRepository;

public interface FilterRepository extends CrudRepository<Filter, Long> {
}
