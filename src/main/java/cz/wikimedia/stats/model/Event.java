package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
public class Event implements IdAble<Long> {

    /* ATTRIBUTES */

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private Collection<EventTag> tags;

    @Column(unique=true, nullable = false)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany
    private Collection<User> participants;

    @ManyToMany
    private Collection<Project> projects;

    @ManyToMany
    private Collection<Revision> revisions;

    private String hashtag;


    /* CONSTRUCTORS */

    protected Event() {}

    public Event(String name) {
        this.name = name;
    }

    public Event(Long id, Collection<EventTag> tags, String name, String hashtag, LocalDate startDate, LocalDate endDate, Collection<User> participants, Collection<Project> projects, Collection<Revision> revisions) {
        this.id = id;
        this.tags = tags;
        this.name = name;
        this.hashtag = hashtag;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
        this.projects = projects;
        this.revisions = revisions;
    }

    public Long getId() {
        return id;
    }



    /* GETTERS */


    public Collection<EventTag> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Collection<User> getParticipants() {
        return participants;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public String getHashtag() {
        return hashtag;
    }

    public Collection<Revision> getRevisions() {
        return revisions;
    }



    /* SETTERS */

    public void setTags(Collection<EventTag> tags) {
        this.tags = tags;
    }

    public Event addTag(EventTag tag) {
        this.tags.add(tag);
        return this;
    }

    public Event removeTag(EventTag tag) {
        this.tags.remove(tag);
        return this;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public Event setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public Event setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Event setParticipants(Collection<User> eventParticipants) {
        this.participants = eventParticipants;
        return this;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Event addParticipant(User participant) {
        this.participants.add(participant);
        return this;
    }

    public Event removeParticipant(User participant) {
        this.participants.remove(participant);
        return this;
    }

    public Event addRevision(Revision rev) {
        this.revisions.add(rev);
        return this;
    }

    public Event removeRevision(Revision rev) {
        this.revisions.remove(rev);
        return this;
    }
}
