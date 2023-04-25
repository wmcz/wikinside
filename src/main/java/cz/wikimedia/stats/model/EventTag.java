package cz.wikimedia.stats.model;

import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class EventTag extends Tag<Event> {

    public EventTag(Long id, String name, Set<Event> tagged, EventTag parent, Set<EventTag> children) {
        super(id, name);
        this.tagged = tagged;
        this.parent = parent;
        this.children = children;
    }

    public EventTag(String name) {
        super(name);
    }

    public EventTag() {
        super();
    }

    @ManyToMany(mappedBy = "tags")
    private Set<Event> tagged;

    @Nullable
    @ManyToOne
    private EventTag parent;

    @OneToMany(mappedBy = "parent")
    private Set<EventTag> children;


    @Override
    public Set<Event> getTagged() {
        return tagged;
    }

    @Override
    public EventTag getParent() {
        return parent;
    }

    @Override
    public Set<EventTag> getChildren() {
        return children;
    }

    @Override
    public EventTag setName(String name) {
        this.name = name;
        return this;
    }
    @Override
    public EventTag setTagged(Set<Event> tagged) {
        this.tagged = tagged;
        return this;
    }

    @Override
    public EventTag addTagged(Event tagged) {
        this.tagged.add(tagged);
        return this;
    }

    @Override
    public EventTag removeTagged(Event tagged) {
        this.tagged.remove(tagged);
        return this;
    }

    @Override
    public EventTag setChildren(Set<Tag<Event>> tags) {
        children.clear();
        tags.forEach(this::addChild);
        return this;
    }

    @Override
    public EventTag addChild(Tag<Event> tag) {
        if (tag instanceof EventTag) children.add((EventTag) tag);
        return this;
    }

    @Override
    public Tag<Event> removeChild(Tag<Event> tag) {
        if (tag instanceof EventTag) children.remove((EventTag) tag);
        return this;
    }

    @Override
    public EventTag setParent(Tag<Event> tag) {
        if (tag instanceof EventTag) {
            parent = (EventTag) tag;
            tag.addChild(this);
        } else if (parent != null) {
            parent.removeChild(this);
            parent = null;
        }
        return this;
    }

}
