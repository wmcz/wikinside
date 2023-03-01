package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.UserTag;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserTagRepository extends CrudRepository<UserTag, Long> {
    Collection<UserTag> findUserTagsByName(String name);
}
