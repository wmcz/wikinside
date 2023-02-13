package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.User;
import cz.cvut.fit.wikimetric.model.UserTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserRepository extends CrudRepository<User, Long> {
    Collection<User> findUsersByUsername(String username);

    @Query(value = "select distinct u from User u where ?1 in u.tags")
    Collection<User> findUsersByTag(UserTag tag);
}
