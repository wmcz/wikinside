package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
