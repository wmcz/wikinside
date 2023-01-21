package cz.cvut.fit.wikimetric.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;

@Entity
public class Event implements IdAble<Long> { //TODO: consider a less ambiguous name (Program? Campaign?)

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

    public Event(Collection<EventTag> tags, String name, Instant startDate, Instant endDate, Collection<User> participants) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public void setParticipants(Collection<User> eventParticipants) {
        this.participants = eventParticipants;
    }
}
