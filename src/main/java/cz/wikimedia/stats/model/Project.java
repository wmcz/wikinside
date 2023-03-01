package cz.wikimedia.stats.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Project implements IdAble<Long> {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String language;

    @Override
    public Long getId() {
        return id;
    }
}
