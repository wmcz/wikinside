package cz.wikimedia.stats.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class GlobalPage {

    @EmbeddedId
    GlobalPageId id;

    public GlobalPage(String wikiUrl, String title) {
        this.id = new GlobalPageId(wikiUrl, title);
    }

    protected GlobalPage() {

    }

    public record GlobalPageId(String wikiUrl, String title) implements Serializable {}
}
