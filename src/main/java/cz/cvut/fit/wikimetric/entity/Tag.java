package cz.cvut.fit.wikimetric.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Tag extends Classifier {

    /* ATTRIBUTES */

    @ManyToMany(mappedBy = "tags")
    Collection<User> users;

    /* CONSTRUCTORS */

    protected Tag() {}

    public Tag(String name) {
        this.name = name;
    }

}
