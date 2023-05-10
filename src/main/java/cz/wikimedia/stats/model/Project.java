package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Project implements IdAble<Long> {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String path;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project project)) return false;
        return Objects.equals(getPath(), project.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }
}
