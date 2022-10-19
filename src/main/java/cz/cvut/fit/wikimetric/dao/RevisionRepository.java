package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Revision;
import org.springframework.data.repository.CrudRepository;

public interface RevisionRepository extends CrudRepository<Revision, Long> {
}
