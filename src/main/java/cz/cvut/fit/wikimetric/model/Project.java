package cz.cvut.fit.wikimetric.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Project {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String language;

}
