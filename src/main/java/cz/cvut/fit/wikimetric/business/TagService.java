package cz.cvut.fit.wikimetric.business;

import cz.cvut.fit.wikimetric.entity.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService extends AbstractService<Tag, Long> {

    public TagService(CrudRepository<Tag, Long> repository) {
        super(repository);
    }
}
