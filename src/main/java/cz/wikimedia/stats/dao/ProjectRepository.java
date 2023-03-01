package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    Collection<Project> findProjectsByName(String name);
    Optional<Project> findProjectByNameAndLanguage(String name, String language);

}
