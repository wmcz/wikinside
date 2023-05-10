package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User implements IdAble<Long> {

    /* ATTRIBUTES */

    @Id
    private Long id; // MediaWiki guiuser id attribute

    private Long localId; // MediaWiki userid for a local wiki defined by GlobalWmClient

    private String username; // need to fetch to ensure it is up-to-date

    private Instant registration;

    @ManyToMany
    private Set<UserTag> tags;

    @ManyToMany(mappedBy = "participants")
    private Set<Event> events;

    @OneToMany(mappedBy = "user")
    private Set<Revision> revisions;


    /* CONSTRUCTORS */

    protected User() {}

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
        this.tags = new HashSet<>();
        this.events = new HashSet<>();
        this.revisions = new HashSet<>();
    }

    public User(Long id, Long localId, String username, Instant registration, Set<UserTag> tags, Set<Event> events) {
        this.id = id;
        this.localId = localId;
        this.username = username;
        this.tags = tags;
        this.events = events;
        this.registration = registration;
        this.revisions = new HashSet<>();
    }



    /* GETTERS */

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Instant getRegistration() {
        return registration;
    }

    public Set<UserTag> getTags() {
        return tags;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Long getLocalId() {
        return localId;
    }

    public Set<Revision> getRevisions() {
        return revisions;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User addTag(UserTag tag) {
        tags.add(tag.addTagged(this));
        return this;
    }

    public User removeTag(UserTag tag) {
        tags.remove(tag.removeTagged(this));
        return this;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public User setLocalId(Long localId) {
        this.localId = localId;
        return this;
    }

    public User setRegistration(Instant registration) {
        this.registration = registration;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
