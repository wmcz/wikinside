package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Long> {
}
