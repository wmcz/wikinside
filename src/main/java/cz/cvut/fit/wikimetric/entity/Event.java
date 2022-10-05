package cz.cvut.fit.wikimetric.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@Entity
public class Event { //TODO: consider a less ambiguous name (Program? Campaign?)

    /* ATTRIBUTES */

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @Nullable
    private EventType eventType;

    @Column(unique=true, nullable = false)
    private String name;

    private Instant startDate;
    private Instant endDate;

    @ManyToMany
    private Collection<User> participants;



    /* CONSTRUCTORS */

    protected Event() {}

    public Event(String name) {
        this.name = name;
    }

    public Event(@Nullable EventType eventType, String name, Instant startDate, Instant endDate, Collection<User> participants) {
        this.eventType = eventType;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }



    /* GETTERS */

    @Nullable
    public EventType getEventType() {
        return eventType;
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

    public void setEventType(@Nullable EventType eventType) {
        this.eventType = eventType;
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
