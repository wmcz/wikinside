package cz.cvut.fit.wikimetric.business;

import cz.cvut.fit.wikimetric.model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService extends AbstractService<Tag, Long> {

    public TagService(CrudRepository<Tag, Long> repository) {
        super(repository);
    }

    public Optional<Tag> assignParent(Tag tag, Tag parent) {
        return update(tag.assignParent(parent));
    }

    public Optional<Tag> removeParent(Tag tag) {
        return update(tag.removeParent());
    }
}
