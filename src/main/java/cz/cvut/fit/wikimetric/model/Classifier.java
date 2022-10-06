package cz.cvut.fit.wikimetric.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Classifier implements IdAble<Long> {

    /* ATTRIBUTES */

    @Column(unique = true, nullable = false)
    protected String name;

    @Id
    @GeneratedValue
    private Long id;



    /* GETTERS */

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    /* SETTERS */

    public void setName(String name) {
        this.name = name;
    }
}
