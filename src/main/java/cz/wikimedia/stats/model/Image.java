package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
public class Image implements IdAble<Long>, Impactable {
    @Id
    private Long pageId;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "images")
    private Set<Event> events;

    private Instant timestamp;

    private String title;

    @ManyToMany
    private Set<ImageCategory> categories;

    @ManyToMany
    private Set<GlobalPage> usage;

    public Image(Long pageId, User user, Set<Event> events, Instant timestamp, String title, Set<GlobalPage> usage, Set<ImageCategory> categories) {
        this.pageId = pageId;
        this.user = user;
        this.events = events;
        this.timestamp = timestamp;
        this.title = title;
        this.usage = usage;
        this.categories = categories;
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

    public Collection<GlobalPage> getUsage() {
        return usage;
    }

    public User getUser() {
        return user;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Set<ImageCategory> getCategories() {
        return categories;
    }

    public Image addCategories(Set<ImageCategory> additional) {
        categories.addAll(additional);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image image)) return false;
        return Objects.equals(getId(), image.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
