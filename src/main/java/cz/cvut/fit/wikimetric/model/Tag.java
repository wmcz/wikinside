package cz.cvut.fit.wikimetric.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag extends Classifier {

    /* ATTRIBUTES */

    private final boolean assignable;
    @ManyToOne
    private Tag parent;

    @OneToMany(mappedBy = "parent")
    private Set<Tag> children;

    @ManyToMany(mappedBy = "tags")
    private Collection<User> users;


    /* CONSTRUCTORS */

    protected Tag() {
        this("", true);
    }

    public Tag(String name, boolean assignable) {
        this.assignable = assignable;
        this.name = name;
        this.children = new HashSet<Tag>();
        this.users = assignable ? new HashSet<>() : Collections.emptySet();
    }
    public Tag(String name) {
        this(name, true);
    }

    public boolean isAssignable() {
        return assignable;
    }

    public Tag removeParent() {

        if (this.parent != null) {
            parent.children.remove(this);
            this.parent = null;
        }

        return this;
    }

    public Tag assignParent(Tag parent) {
        this.removeParent();
        parent.children.add(this);
        this.parent = parent;
        return this;
    }

    public Tag addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Tag removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Collection<Tag> getChildren() {
        return children;
    }

    public Collection<User> getUsers() {
        return users;
    }
}
