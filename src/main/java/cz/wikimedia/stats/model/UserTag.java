package cz.wikimedia.stats.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
public class UserTag extends Tag<User> {

    public UserTag(Long id, String name, Set<User> tagged, UserTag parent, Set<UserTag> children) {
        super(id, name);
        this.tagged = tagged;
        this.parent = parent;
        this.children = children;
    }
    public UserTag() {
        super();
    }

    @ManyToMany(mappedBy = "tags")
    private Set<User> tagged;

    @Nullable
    @ManyToOne
    private UserTag parent;

    @OneToMany(mappedBy = "parent")
    private Set<UserTag> children;


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
    public UserTag setName(String name) {
        this.name = name;
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
