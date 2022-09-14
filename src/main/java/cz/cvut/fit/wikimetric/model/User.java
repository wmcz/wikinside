package cz.cvut.fit.wikimetric.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
public class User {

    /* ATTRIBUTES */

    @Id
    private Long ID; // MediaWiki user_id user attribute

    @ManyToMany(mappedBy = "userID")
    private Collection<Tag> tags;

    @ManyToMany(mappedBy = "userID")
    private Collection<Event> events;



    /* CONSTRUCTORS */

    protected User() {}

    public User(Long ID) {
        this.ID = ID;
    }

    public User(Long ID, Collection<Tag> tags, Collection<Event> events) {
        this.ID = ID;
        this.tags = tags;
        this.events = events;
    }



    /* GETTERS */

    public Long getID() {
        return ID;
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
}
