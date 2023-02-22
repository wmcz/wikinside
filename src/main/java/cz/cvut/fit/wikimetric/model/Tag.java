package cz.cvut.fit.wikimetric.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Tag<E> implements IdAble<Long> {

    /* ATTRIBUTES */

    @Column(unique = true, nullable = false)
    protected String name;

    @Id
    @GeneratedValue
    private Long id;

    private final boolean assignable;

    protected Tag(Long id, String name, boolean assignable) {
        this.id = id;
        this.name = name;
        this.assignable = assignable;
    }

    protected Tag(String name, boolean assignable) {
        this.name = name;
        this.assignable = assignable;
    }

    protected Tag() {
        this("", true);
    }

    protected Tag(String name) {
        this(name, true);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAssignable() {
        return assignable;
    }

    public abstract Collection<E> getTagged();

    public abstract Tag<E> getParent();

    public abstract Collection<? extends Tag<E>> getChildren();

    /* SETTERS */

    public void setName(String name) {
        this.name = name;
    }

    public abstract Tag<E> setTagged(Collection<E> tagged);
    public abstract Tag<E> addTagged(E tagged);
    public abstract Tag<E> removeTagged(E tagged);
    public abstract Tag<E> setChildren(Collection<Tag<E>> tags);

    public abstract Tag<E> addChild(Tag<E> tag);
    public abstract Tag<E> removeChild(Tag<E> tag);
    public abstract Tag<E> setParent(@Nullable Tag<E> tag);

}
