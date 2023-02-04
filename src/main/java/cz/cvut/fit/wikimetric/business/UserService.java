package cz.cvut.fit.wikimetric.business;

import cz.cvut.fit.wikimetric.dao.UserRepository;
import cz.cvut.fit.wikimetric.model.UserTag;
import cz.cvut.fit.wikimetric.model.User;

import java.util.Collection;
import java.util.Optional;


public class UserService extends AbstractService<User, Long> {
    private final UserRepository userRepository;
    public UserService(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }

    public Collection<User> findByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    public Optional<User> addTag(User user, UserTag tag) {
        return update(user.addTag(tag));
    }

    public Optional<User> removeTag(User user, UserTag tag) {
        return update(user.removeTag(tag));
    }
}
