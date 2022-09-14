package cz.cvut.fit.wikimetric.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EventType {

    /* ATTRIBUTES */

    @Id
    @GeneratedValue
    private Long ID;

    @Column(unique = true, nullable = false)
    private String name;



    /* CONSTRUCTORS */

    protected EventType() {}

    public EventType(String name) {
        this.name = name;
    }



    /* GETTERS */

    public Long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }



    /* SETTERS */

    public void setName(String name) {
        this.name = name;
    }
}
