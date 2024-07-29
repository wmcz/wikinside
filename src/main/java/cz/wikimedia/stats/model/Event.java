package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Event implements IdAble<Long> {

    public enum DataCollectionStrategy {
        MANUAL,
        HASHTAG,
        PHOTO
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<EventTag> tags;

    @ManyToMany
    private Set<UserTag> userTags;

    @Column(unique=true, nullable = false)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany
    private Set<User> participants;

    @ManyToMany
    private Set<Project> projects;

    @ManyToMany
    private Set<Revision> revisions;

    @ManyToMany
    private Set<Image> images;

    private DataCollectionStrategy strategy;

    private String category;

    protected Event() {}

    public Event(Long id, Set<EventTag> tags, Set<UserTag> userTags, String name, DataCollectionStrategy strategy, String category, LocalDate startDate, LocalDate endDate, Set<User> participants, Set<Project> projects, Set<Revision> revisions, Set<Image> images) {
        this.id = id;
        this.tags = tags;
        this.userTags = userTags;
        this.name = name;
        this.strategy = strategy;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
        this.projects = projects;
        this.revisions = revisions;
        this.images = images;
    }

    private Set<User> getActiveParticipants() {
        return getImpactables()
                .stream()
                .map(Impactable::getUser)
                .collect(Collectors.toSet());
    }

    private void removeIfInactive(User user) {
        if (
                getStrategy() != DataCollectionStrategy.MANUAL &&
                !getActiveParticipants().contains(user)
        )
            participants.remove(user);
    }

    public Long getId() {
        return id;
    }

    public Set<EventTag> getTags() {
        return tags;
    }

    public Set<UserTag> getUserTags() {
        return userTags;
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

    public Set<User> getParticipants() {
        return participants;
    }

    public Set<Impactable> getImpactables() {
        Set<Impactable> res = new HashSet<>();
        res.addAll(getRevisions());
        res.addAll(getImages());
        return res;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public DataCollectionStrategy getStrategy() {
        return strategy;
    }

    public String getCategory() {
        return category;
    }

    public Set<Revision> getRevisions() {
        return revisions;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Event addTag(EventTag tag) {
        tags.add(tag);
        return this;
    }

    public Event removeTag(EventTag tag) {
        tags.remove(tag);
        return this;
    }

    public Event addParticipant(User participant) {
        if (getStrategy() == DataCollectionStrategy.MANUAL)
            participants.add(participant);
        return this;
    }

    public Event removeParticipant(User participant) {
        if (getStrategy() == DataCollectionStrategy.MANUAL) {
            participants.remove(participant);
            revisions.removeIf(r -> r.getUser().equals(participant));
            images.removeIf(i -> i.getUser().equals(participant));

        }
        return this;
    }

    public Event addRevision(Revision rev) {
        revisions.add(rev);
        if (getStrategy() != DataCollectionStrategy.MANUAL)
            participants.add(rev.getUser());
        return this;
    }

    public Event removeRevision(Revision rev) {
        revisions.remove(rev);
        removeIfInactive(rev.getUser());
        return this;
    }

    public Event addImage(Image image) {
        images.add(image);
        if (getStrategy() != DataCollectionStrategy.MANUAL)
            participants.add(image.getUser());
        return this;
    }

    public Event removeImage(Image image) {
        images.remove(image);
        removeIfInactive(image.getUser());
        return this;
    }

    public Event addUserTag(UserTag tag) {
        userTags.add(tag);
        return this;
    }

    public Event removeUserTag(UserTag tag) {
        userTags.remove(tag);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
