package cz.cvut.fit.wikimetric.entity;

import javax.persistence.Entity;

@Entity
public class EventType extends Classifier {

    /* CONSTRUCTORS */

    protected EventType() {}

    public EventType(String name) {
        this.name = name;
    }

}
