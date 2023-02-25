package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.UserTag;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserTagRepository extends CrudRepository<UserTag, Long> {
    Collection<UserTag> findUserTagsByName(String name);
}
