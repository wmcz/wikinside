package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends InternalService<Project, Long> {

    public ProjectService(CrudRepository<Project, Long> repository) {
        super(repository);
    }
}
