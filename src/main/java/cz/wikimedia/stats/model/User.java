package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;

@Entity
public class User implements IdAble<Long> {

    /* ATTRIBUTES */

    @Id
    private Long id; // MediaWiki guiuser id attribute

    private Long localId; // MediaWiki userid for a local wiki defined by GlobalWmClient

    private String username; // need to fetch to ensure it is up-to-date

    private Instant registration;

    @ManyToMany
    private Collection<UserTag> tags;

    @ManyToMany(mappedBy = "participants")
    private Collection<Event> events;

    @OneToMany(mappedBy = "user")
    private Collection<Revision> revisions;


    /* CONSTRUCTORS */

    protected User() {}

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
        this.tags = new HashSet<>();
        this.events = new HashSet<>();
    }

    public User(Long id, Long localId, String username, Instant registration, Collection<UserTag> tags, Collection<Event> events) {
        this.id = id;
        this.localId = localId;
        this.username = username;
        this.tags = tags;
        this.events = events;
        this.registration = registration;
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

    public Collection<UserTag> getTags() {
        return tags;
    }

    public Collection<Event> getEvents() {
        return events;
    }

    public Long getLocalId() {
        return localId;
    }

    public Collection<Revision> getRevisions() {
        return revisions;
    }


    /* SETTERS */

    public User setUsername(String username) {
        this.username = username;
        return this;
    }
    public User setTags(Collection<UserTag> tags) {
        this.tags = tags;
        return this;
    }

    public User setEvents(Collection<Event> events) {
        this.events = events;
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
}
