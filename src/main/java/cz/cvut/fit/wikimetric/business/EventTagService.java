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
        return update(tag.setParent(parent));
    }
}
