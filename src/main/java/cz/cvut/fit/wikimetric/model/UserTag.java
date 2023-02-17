package cz.cvut.fit.wikimetric.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class UserTag extends Tag<User> {

    public UserTag(Long id, String name, boolean assignable, Collection<User> tagged, UserTag parent, Collection<UserTag> children) {
        super(id, name, assignable);
        this.tagged = tagged;
        this.parent = parent;
        this.children = children;
    }

    public UserTag(String name, boolean assignable) {
        super(name, assignable);
    }

    public UserTag() {
        super();
    }

    public UserTag(String name) {
        super(name);
    }


    @ManyToMany(mappedBy = "tags")
    private Collection<User> tagged;

    @ManyToOne
    private UserTag parent;

    @OneToMany(mappedBy = "parent")
    private Collection<UserTag> children;


    @Override
    public Collection<User> getTagged() {
        return tagged;
    }

    @Override
    public UserTag getParent() {
        return parent;
    }

    @Override
    public Collection<UserTag> getChildren() {
        return children;
    }

    @Override
    public UserTag setTagged(Collection<User> tagged) {
        this.tagged = tagged;
        return this;
    }

    @Override
    public UserTag addTagged(User tagged) {
        this.tagged.add(tagged);
        return this;
    }

    @Override
    public UserTag removeTagged(User tagged) {
        this.tagged.remove(tagged);
        return this;
    }

    @Override
    public UserTag setChildren(Collection<Tag<User>> tags) {
        children.clear();
        tags.forEach(this::addChild);
        return this;
    }

    @Override
    public UserTag addChild(Tag<User> tag) {
        if (tag instanceof UserTag) children.add((UserTag) tag);
        return this;
    }

    @Override
    public Tag<User> removeChild(Tag<User> tag) {
        if (tag instanceof UserTag) children.remove((UserTag) tag);
        return this;
    }

    @Override
    public UserTag setParent(Tag<User> tag) {
        if (tag instanceof UserTag) {
            parent = (UserTag) tag;
            tag.addChild(this);
        } else if (parent != null) {
            parent.removeChild(this);
            parent = null;
        }
        return this;
    }
}
