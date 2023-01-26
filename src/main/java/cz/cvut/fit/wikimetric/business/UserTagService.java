package cz.cvut.fit.wikimetric.business;


import cz.cvut.fit.wikimetric.model.UserTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTagService extends AbstractService<UserTag, Long> {
    public UserTagService(CrudRepository<UserTag, Long> repository) {
        super(repository);
    }

    public Optional<UserTag> setParent(UserTag tag, UserTag parent) {
        Optional<UserTag> res = update(tag.setParent(parent));
        if (update(parent).isEmpty())
            throw new IllegalStateException("Inconsistent data: parent-child relationship could not be established properly for the following tags:" + parent.getId() + ", " + tag.getId());
        return res;
    }
}
