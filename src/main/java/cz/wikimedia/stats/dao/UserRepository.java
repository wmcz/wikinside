package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserRepository extends CrudRepository<User, Long> {
    Collection<User> findUsersByUsername(String username);
}
