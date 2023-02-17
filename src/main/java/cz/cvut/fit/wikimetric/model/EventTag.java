package cz.cvut.fit.wikimetric.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class EventTag extends Tag<Event> {

    public EventTag(Long id, String name, boolean assignable, Collection<Event> tagged, EventTag parent, Collection<EventTag> children) {
        super(id, name, assignable);
        this.tagged = tagged;
        this.parent = parent;
        this.children = children;
    }

    public EventTag(String name, boolean assignable) {
        super(name, assignable);
    }

    public EventTag() {
        super();
    }

    public EventTag(String name) {
        super(name);
    }


    @ManyToMany(mappedBy = "tags")
    private Collection<Event> tagged;

    @ManyToOne
    private EventTag parent;

    @OneToMany(mappedBy = "parent")
    private Collection<EventTag> children;


    @Override
    public Collection<Event> getTagged() {
        return tagged;
    }

    @Override
    public EventTag getParent() {
        return parent;
    }

    @Override
    public Collection<EventTag> getChildren() {
        return children;
    }

    @Override
    public EventTag setTagged(Collection<Event> tagged) {
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
    public EventTag setChildren(Collection<Tag<Event>> tags) {
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
