package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {}
