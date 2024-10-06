package cz.wikimedia.stats.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Objects;
import java.util.Set;

@Entity
public class ImageCategory implements IdAble<String> {
    @Id
    private String title;

    @ManyToMany(mappedBy = "categories")
    private Set<Image> images;

    protected ImageCategory() {}

    public ImageCategory(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageCategory that)) return false;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }

    @Override
    public String getId() {
        return title;
    }
}
