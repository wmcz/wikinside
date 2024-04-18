package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"revId", "project_id"})})
public class Revision implements IdAble<Long>, Impactable {
    @Id
    @GeneratedValue
    private Long id;

    private Long revId; // is not unique across projects

    private Long diff;

    private Long pageId;

    private Long parentId; // the revision immediately before on a given page

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "revisions")
    private Set<Event> events;

    @ManyToOne
    private Project project;

    private Instant timestamp;

    @Column(length = 512)
    private String summary;

    protected Revision() {}

       public Revision(Long id, Long revId, Long diff, Long pageId, Long parentId, User user, Set<Event> events, Project project, Instant timestamp, String summary) {
        this.id = id;
        this.revId = revId;
        this.diff = diff;
        this.pageId = pageId;
        this.parentId = parentId;
        this.user = user;
        this.events = events;
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

    public Revision setDiff(Long diff) {
        this.diff = diff;
        return this;
    }

    public Long getPageId() {
        return pageId;
    }

    public Long getParentId() {
        return parentId;
    }

    public Boolean isPageCreation() {
        return parentId == 0;
    }

    public User getUser() {
        return user;
    }

    public Set<Event> getEvents() {
        return events;
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

    public Revision addEvent(Event event) {
        if (!events.contains(event)) this.events.add(event);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Revision revision)) return false;
        return Objects.equals(getRevId(), revision.getRevId()) && Objects.equals(getProject(), revision.getProject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRevId(), getProject());
    }
}
