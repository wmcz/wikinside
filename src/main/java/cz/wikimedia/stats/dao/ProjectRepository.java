package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    Optional<Project> findByPath(String path);
}
