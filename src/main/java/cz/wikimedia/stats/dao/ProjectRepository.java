package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Project;
import org.springframework.data.repository.CrudRepository;
public interface ProjectRepository extends CrudRepository<Project, Long> {

}
