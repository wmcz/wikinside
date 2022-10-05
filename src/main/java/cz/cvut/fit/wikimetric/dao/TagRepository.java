package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Long> {
}
