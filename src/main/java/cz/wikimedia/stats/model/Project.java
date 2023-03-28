package cz.wikimedia.stats.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class Project implements IdAble<Long> {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String path;

    @OneToMany(mappedBy = "project")
    private Collection<Revision> revisions;

    protected Project() {}

    public Project(Long id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
