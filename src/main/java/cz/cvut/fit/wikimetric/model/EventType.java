package cz.cvut.fit.wikimetric.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EventType extends Classifier {

    /* CONSTRUCTORS */

    protected EventType() {}

    public EventType(String name) {
        this.name = name;
    }

}
