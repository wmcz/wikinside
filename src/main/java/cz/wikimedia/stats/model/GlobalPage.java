package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"wikiUrl", "title"})})
public class GlobalPage implements IdAble<Long> {

    @Id
    @GeneratedValue
    private Long id;

    private String wikiUrl;

    private String title;

    public GlobalPage(String wikiUrl, String title) {
        this.wikiUrl = wikiUrl;
        this.title = title;
    }

    protected GlobalPage() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlobalPage that)) return false;
        return Objects.equals(wikiUrl, that.wikiUrl) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wikiUrl, title);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getUrl() {
        return wikiUrl;
    }

    public String getTitle() {
        return title;
    }
}
