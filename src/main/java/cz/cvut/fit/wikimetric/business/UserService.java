package cz.cvut.fit.wikimetric.business;

import cz.cvut.fit.wikimetric.model.UserTag;
import cz.cvut.fit.wikimetric.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public class UserService extends AbstractService<User, Long> {

    public UserService(CrudRepository<User, Long> repository) {
        super(repository);
    }

    public Optional<User> addTag(User user, UserTag tag) {
        return update(user.addTag(tag));
    }

    public Optional<User> removeTag(User user, UserTag tag) {
        return update(user.removeTag(tag));
    }
}
