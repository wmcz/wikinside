package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.Instant;
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

    private Instant startDate;
    private Instant endDate;

    @ManyToMany
    private Collection<User> participants;

    @Transient
    private EventImpact impact;


    /* CONSTRUCTORS */

    protected Event() {}

    public Event(String name) {
        this.name = name;
    }

    public Event(Long id, Collection<EventTag> tags, String name, Instant startDate, Instant endDate, Collection<User> participants) {
        this.id = id;
        this.tags = tags;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
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

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Collection<User> getParticipants() {
        return participants;
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

    public Event setStartDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public Event setEndDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public Event setParticipants(Collection<User> eventParticipants) {
        this.participants = eventParticipants;
        return this;
    }

    public Event addParticipant(User participant) {
        this.participants.add(participant);
        return this;
    }

    public Event removeParticipant(User participant) {
        this.participants.remove(participant);
        return this;
    }
}
