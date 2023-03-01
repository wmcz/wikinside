package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import cz.wikimedia.stats.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface RevisionRepository extends CrudRepository<Revision, Long> {
    Collection<Revision> findRevisionsByUser(User user);
    Collection<Revision> findRevisionsByEvent(Event event);
    Collection<Revision> findRevisionsByProject(Project project);
    Collection<Revision> findRevisionsByUserAndEvent(User user, Event event);

    Collection<Revision> findRevisionsByUserAndEventAndProject(User user, Event event, Project project);
}
