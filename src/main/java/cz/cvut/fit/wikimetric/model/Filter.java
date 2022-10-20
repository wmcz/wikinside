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
    private Long id;

    private String name;

    @ManyToMany
    private Collection<Tag> includedTags;

    @ManyToMany
    private Collection<Tag> excludedTags;

    @ManyToMany
    private Collection<EventType> includedEventTypes;

    @ManyToMany
    private Collection<EventType> excludedEventTypes;

    @ManyToMany
    private Collection<Project> includedProjects;

    @ManyToMany
    private Collection<Project> excludedProjects;
}
