package cz.cvut.fit.wikimetric.model;

import javax.persistence.Entity;

@Entity
public class EventType extends Classifier {

    /* CONSTRUCTORS */

    protected EventType() {}

    public EventType(String name) {
        this.name = name;
    }

}
