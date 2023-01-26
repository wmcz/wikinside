package cz.cvut.fit.wikimetric.business;


import cz.cvut.fit.wikimetric.model.EventTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventTagService extends AbstractService<EventTag, Long> {
    public EventTagService(CrudRepository<EventTag, Long> repository) {
        super(repository);
    }

    public Optional<EventTag> setParent(EventTag tag, EventTag parent) {
        Optional<EventTag> res = update(tag.setParent(parent));
        if (update(parent).isEmpty())
            throw new IllegalStateException("Inconsistent data: parent-child relationship could not be established properly for the following tags:" + parent.getId() + ", " + tag.getId());
        return res;
    }
}
