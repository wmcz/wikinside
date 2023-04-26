package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.ProjectRepository;
import cz.wikimedia.stats.model.Project;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends InternalService<Project, Long> {

    public ProjectService(ProjectRepository repository) {
        super(repository);
    }
}
