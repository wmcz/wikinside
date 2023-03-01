package cz.wikimedia.stats.model;

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
    private Collection<UserTag> includedUserTags;

    @ManyToMany
    private Collection<UserTag> excludedUserTags;

    @ManyToMany
    private Collection<EventTag> includedEventTags;

    @ManyToMany
    private Collection<EventTag> excludedEventTags;

    @ManyToMany
    private Collection<Project> includedProjects;

    @ManyToMany
    private Collection<Project> excludedProjects;
}
