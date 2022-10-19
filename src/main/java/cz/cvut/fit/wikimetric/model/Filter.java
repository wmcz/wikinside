package cz.cvut.fit.wikimetric.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
public class Filter {
    @Id
    @GeneratedValue
    Long id;

    @ManyToMany
    Collection<Tag> includedTags;

    @ManyToMany
    Collection<Tag> excludedTags;

    @ManyToMany
    Collection<EventType> includedEventTypes;

    @ManyToMany
    Collection<EventType> excludedEventTypes;

    @ManyToMany
    Collection<Project> includedProjects;

    @ManyToMany
    Collection<Project> excludedProjects;
}
