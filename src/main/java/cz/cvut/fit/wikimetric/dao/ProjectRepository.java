package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    Collection<Project> findProjectsByName(String name);
    Optional<Project> findProjectByNameAndLanguage(String name, String language);

}
