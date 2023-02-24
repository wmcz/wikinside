package cz.cvut.fit.wikimetric.business;


import cz.cvut.fit.wikimetric.dao.UserRepository;
import cz.cvut.fit.wikimetric.dao.UserTagRepository;
import cz.cvut.fit.wikimetric.model.IdAble;
import cz.cvut.fit.wikimetric.model.User;
import cz.cvut.fit.wikimetric.model.UserTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserTagService extends AbstractService<UserTag, Long> {

    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    public UserTagService(UserTagRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.userTagRepository = repository;
    }

    private <T extends IdAble<Long>> Collection<T> updateNonOwningField(UserTag tag,
                                                                        Collection<Long> ids,
                                                                        Function<T, T> updatingFunc,
                                                                        CrudRepository<T, Long> repository,
                                                                        Function<UserTag, Collection<T>> fieldAccessFunc) {
        return updateNonOwningField(tag, ids, updatingFunc, repository, userTagRepository, fieldAccessFunc);
    }

    private Collection<User> updateUsers(UserTag tag, Collection<Long> userIds, Function<User, User> function) {
        return updateNonOwningField(tag, userIds, function, userRepository, UserTag::getTagged);
    }

    private Collection<UserTag> updateChildren(UserTag tag, Collection<Long> userIds, UserTag newParent) {
        return updateNonOwningField(tag, userIds, c -> c.setParent(newParent), userTagRepository, UserTag::getChildren);
    }

    public Optional<UserTag> setParent(UserTag tag, UserTag parent) {
        Optional<UserTag> res = update(tag.setParent(parent));
        if (update(parent).isEmpty())
            throw new IllegalStateException("Inconsistent data: parent-child relationship could not be established properly for the following tags:" + parent.getId() + ", " + tag.getId());
        return res;
    }

    public Collection<User> getUsersWithTag(UserTag tag) {
        Collection<User> result = tag.getTagged();
        tag.getChildren().forEach(child -> result.addAll(getUsersWithTag(child)));
        return result;
    }


    public Collection<UserTag> findByName(String name) {
        return userTagRepository.findUserTagsByName(name);
    }

    public Collection<User> addUsers(UserTag tag, Collection<Long> userIds) {
        return updateUsers(tag, userIds, u -> u.addTag(tag));
    }

    public Collection<User> removeUsers(UserTag tag, Collection<Long> userIds) {
        return updateUsers(tag, userIds, u -> u.removeTag(tag));
    }

    public Collection<UserTag> addChildren(UserTag tag, Collection<Long> childrenIds) {
        return updateChildren(tag, childrenIds, tag);
    }

    public Collection<UserTag> removeChildren(UserTag tag, Collection<Long> childrenIds) {
        return updateChildren(tag, childrenIds, null);
    }
}
