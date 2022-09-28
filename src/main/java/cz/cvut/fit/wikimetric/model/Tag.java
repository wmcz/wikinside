package cz.cvut.fit.wikimetric.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Tag extends Classifier {

    /* ATTRIBUTES */

    @ManyToMany
    Collection<User> users;

    /* CONSTRUCTORS */

    protected Tag() {}

    public Tag(String name) {
        this.name = name;
    }

}
