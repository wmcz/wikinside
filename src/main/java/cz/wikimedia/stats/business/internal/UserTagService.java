package cz.wikimedia.stats.business.internal;


import cz.wikimedia.stats.dao.UserRepository;
import cz.wikimedia.stats.dao.UserTagRepository;
import cz.wikimedia.stats.model.IdAble;
import cz.wikimedia.stats.model.User;
import cz.wikimedia.stats.model.UserTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserTagService extends InternalService<UserTag, Long> {

    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    public UserTagService(UserTagRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.userTagRepository = repository;
    }

    private <T extends IdAble<Long>> Collection<T> applyToNonOwningField(UserTag tag,
                                                                         Collection<Long> ids,
                                                                         Function<T, T> updatingFunc,
                                                                         CrudRepository<T, Long> repository,
                                                                         Function<UserTag, Collection<T>> fieldAccessFunc) {
        return applyToNonOwningField(tag, ids, updatingFunc, repository, userTagRepository, fieldAccessFunc);
    }

    private Collection<User> applyUsers(UserTag tag, Collection<Long> userIds, Function<User, User> function) {
        return applyToNonOwningField(tag, userIds, function, userRepository, UserTag::getTagged);
    }

    private Collection<UserTag> applyChildren(UserTag tag, Collection<Long> userIds, UserTag newParent) {
        return applyToNonOwningField(tag, userIds, c -> c.setParent(newParent), userTagRepository, UserTag::getChildren);
    }

    private void updateUsers(UserTag tag, Collection<User> updated) {
        updateNonOwningField(tag, updated, UserTag::getTagged, this::addUsers, this::removeUsers);
    }

    private void updateChildren(UserTag tag, Collection<UserTag> updated) {
        updateNonOwningField(tag, updated, UserTag::getChildren, this::addChildren, this::removeChildren);
    }

    public Collection<User> addUsers(UserTag tag, Collection<Long> userIds) {
        return applyUsers(tag, userIds, u -> u.addTag(tag));
    }

    public Collection<User> removeUsers(UserTag tag, Collection<Long> userIds) {
        return applyUsers(tag, userIds, u -> u.removeTag(tag));
    }

    public Collection<UserTag> addChildren(UserTag tag, Collection<Long> childrenIds) {
        return applyChildren(tag, childrenIds, tag);
    }

    public Collection<UserTag> removeChildren(UserTag tag, Collection<Long> childrenIds) {
        return applyChildren(tag, childrenIds, null);
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(t -> {
            removeUsers   (t, t.getTagged()  .stream().map(User::getId)   .toList());
            removeChildren(t, t.getChildren().stream().map(UserTag::getId).toList());
        });
        super.deleteById(id);
    }

    @Override
    public <S extends UserTag> Optional<S> create(S tag) {
        Optional<S> res = super.create(tag);
        if (res.isPresent()) {
            addUsers   (res.get(), toIds(tag.getTagged()));
            addChildren(res.get(), toIds(tag.getChildren()));
            return update(res.get());

        } else return res;
    }

    @Override
    public <S extends UserTag> Optional<S> update(S tag) {
        Optional<UserTag> current = findById(tag.getId());

        if (current.isEmpty()) return Optional.empty();
        else {
            updateUsers   (current.get(), tag.getTagged());
            updateChildren(current.get(), tag.getChildren());
            return super.update(tag);
        }
    }
}
