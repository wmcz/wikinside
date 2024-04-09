package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;

@Entity
public class Image implements IdAble<Long> {
    @Id
    private Long pageId;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "images")
    private Set<Event> events;

    private Instant timestamp;

    private String title;

    @ManyToMany
    private Set<GlobalPage> usage;

    public Image(Long pageId, User user, Set<Event> events, Instant timestamp, String title, Set<GlobalPage> usage) {
        this.pageId = pageId;
        this.user = user;
        this.events = events;
        this.timestamp = timestamp;
        this.title = title;
        this.usage = usage;
    }

    protected Image() {}

    @Override
    public Long getId() {
        return pageId;
    }

    public Collection<Event> getEvents() {
        return events;
    }

    public Collection<Event> addEvent(Event event) {
        events.add(event);
        return events;
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }
}
