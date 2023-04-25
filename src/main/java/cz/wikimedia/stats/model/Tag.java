package cz.wikimedia.stats.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Tag<E> implements IdAble<Long> {

    /* ATTRIBUTES */

    @Column(unique = true, nullable = false)
    protected String name;

    @Id
    @GeneratedValue
    private Long id;

    protected Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Tag(String name) {
        this.name = name;
    }

    protected Tag() {
        this("");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract Collection<E> getTagged();

    public abstract Tag<E> getParent();

    public abstract Collection<? extends Tag<E>> getChildren();

    /* SETTERS */

    public abstract Tag<E> setName(String name);

    public abstract Tag<E> addTagged(E tagged);
    public abstract Tag<E> removeTagged(E tagged);

    public abstract Tag<E> addChild(Tag<E> tag);
    public abstract Tag<E> removeChild(Tag<E> tag);
    public abstract Tag<E> setParent(@Nullable Tag<E> tag);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag<?> tag)) return false;
        return Objects.equals(getId(), tag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
