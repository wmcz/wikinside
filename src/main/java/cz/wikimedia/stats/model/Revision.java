package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.Instant;

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

    private Instant timestamp;

    @Column(length = 512)
    private String summary;

    protected Revision() {}

       public Revision(Long id, Long revId, Long diff, User user, Event event, Project project, Instant timestamp, String summary) {
        this.id = id;
        this.revId = revId;
        this.diff = diff;
        this.user = user;
        this.event = event;
        this.project = project;
        this.timestamp = timestamp;
        this.summary = summary;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSummary() {
        return summary;
    }

    public Revision setEvent(Event event) {
        this.event = event;
        return this;
    }
}
