package cz.cvut.fit.wikimetric.dao;

import cz.cvut.fit.wikimetric.model.Event;
import cz.cvut.fit.wikimetric.model.Project;
import cz.cvut.fit.wikimetric.model.Revision;
import cz.cvut.fit.wikimetric.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface RevisionRepository extends CrudRepository<Revision, Long> {
    Collection<Revision> findRevisionsByUser(User user);
    Collection<Revision> findRevisionsByEvent(Event event);
    Collection<Revision> findRevisionsByProject(Project project);
    Collection<Revision> findRevisionsByUserAndEvent(User user, Event event);

    Collection<Revision> findRevisionsByUserAndEventAndProject(User user, Event event, Project project);
}
