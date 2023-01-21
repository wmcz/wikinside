package cz.cvut.fit.wikimetric.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.Collection;

@Entity
public class User implements IdAble<Long> {

    /* ATTRIBUTES */

    @Id
    private Long id; // MediaWiki user_id user attribute

    @ManyToMany
    private Collection<UserTag> tags;

    @ManyToMany(mappedBy = "participants")
    private Collection<Event> events;

    @Transient
    private UserImpact impact;



    /* CONSTRUCTORS */

    protected User() {}

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, Collection<UserTag> tags, Collection<Event> events) {
        this.id = id;
        this.tags = tags;
        this.events = events;
    }



    /* GETTERS */

    public Long getId() {
        return id;
    }

    public Collection<UserTag> getTags() {
        return tags;
    }

    public Collection<Event> getEvents() {
        return events;
    }



    /* SETTERS */

    public void setTags(Collection<UserTag> tags) {
        this.tags = tags;
    }

    public void setEvents(Collection<Event> events) {
        this.events = events;
    }

    public User addTag(UserTag tag) {
        tags.add(tag.addTagged(this));
        return this;
    }

    public User removeTag(UserTag tag) {
        tags.remove(tag.removeTagged(this));
        return this;
    }
}
