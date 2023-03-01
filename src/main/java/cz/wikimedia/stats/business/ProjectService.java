package cz.wikimedia.stats.business;

import cz.wikimedia.stats.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends AbstractService<Project, Long> {

    public ProjectService(CrudRepository<Project, Long> repository) {
        super(repository);
    }
}
