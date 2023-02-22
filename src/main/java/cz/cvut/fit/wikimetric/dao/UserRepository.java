package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserRepository extends CrudRepository<User, Long> {
    Collection<User> findUsersByUsername(String username);
}
