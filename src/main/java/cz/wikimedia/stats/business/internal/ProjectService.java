package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.ProjectRepository;
import cz.wikimedia.stats.model.Project;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService extends InternalService<Project, Long> {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository repository) {
        super(repository);
        this.projectRepository = repository;
    }

    public Optional<Project> findByPath(String path) {
        return projectRepository.findByPath(path);
    }
}
