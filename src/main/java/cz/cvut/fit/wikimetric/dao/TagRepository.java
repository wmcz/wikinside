package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface TagRepository extends CrudRepository<Tag, Long> {
    Collection<Tag> findTagsByName(String name);
}
