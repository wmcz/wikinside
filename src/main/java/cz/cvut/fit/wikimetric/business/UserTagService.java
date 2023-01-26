package cz.cvut.fit.wikimetric.business;


import cz.cvut.fit.wikimetric.dao.UserRepository;
import cz.cvut.fit.wikimetric.model.User;
import cz.cvut.fit.wikimetric.model.UserTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserTagService extends AbstractService<UserTag, Long> {

    private final UserRepository userRepository;
    public UserTagService(CrudRepository<UserTag, Long> repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    public Optional<UserTag> setParent(UserTag tag, UserTag parent) {
        Optional<UserTag> res = update(tag.setParent(parent));
        if (update(parent).isEmpty())
            throw new IllegalStateException("Inconsistent data: parent-child relationship could not be established properly for the following tags:" + parent.getId() + ", " + tag.getId());
        return res;
    }

    public Collection<User> getUsersWithTags(Collection<UserTag> tags) {
        return userRepository.findUsersByTagsContains(tags);
    }
}
