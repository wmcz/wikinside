package cz.wikimedia.stats.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Revision implements IdAble<Long> {
    @Id
    @GeneratedValue
    private Long id;

    private Long revId; // is not unique across projects

    private Long diff;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Project project;

    protected Revision() {}

    public Revision(Long id, Long revId, Long diff, User user, Event event, Project project) {
        this.id = id;
        this.revId = revId;
        this.diff = diff;
        this.user = user;
        this.event = event;
        this.project = project;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getRevId() {
        return revId;
    }

    public Long getDiff() {
        return diff;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public Project getProject() {
        return project;
    }
}
