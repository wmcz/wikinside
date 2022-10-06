package cz.cvut.fit.wikimetric.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
public class User implements IdAble<Long> {

    /* ATTRIBUTES */

    @Id
    private Long id; // MediaWiki user_id user attribute

    @ManyToMany
    private Collection<Tag> tags;

    @ManyToMany(mappedBy = "participants")
    private Collection<Event> events;



    /* CONSTRUCTORS */

    protected User() {}

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, Collection<Tag> tags, Collection<Event> events) {
        this.id = id;
        this.tags = tags;
        this.events = events;
    }



    /* GETTERS */

    public Long getId() {
        return id;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public Collection<Event> getEvents() {
        return events;
    }



    /* SETTERS */

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public void setEvents(Collection<Event> events) {
        this.events = events;
    }

    public User addTag(Tag tag) {
        tags.add(tag);
        return this;
    }

    public User removeTag(Tag tag) {
        tags.remove(tag);
        return this;
    }
}
