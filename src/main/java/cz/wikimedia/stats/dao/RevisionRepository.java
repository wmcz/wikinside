package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RevisionRepository extends CrudRepository<Revision, Long> {
    Optional<Revision> findRevisionByRevIdAndProject(Long revId, Project project);
    Optional<Revision> findTopRevisionByEventOrderByTimestampDesc(Event event);
}
