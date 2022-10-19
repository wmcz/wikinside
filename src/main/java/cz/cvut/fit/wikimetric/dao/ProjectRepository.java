package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
