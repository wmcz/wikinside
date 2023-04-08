package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record PageQuery(@JsonProperty("pages") Collection<WmPage> pages) implements Query<Collection<WmPage>> {
    @Override
    public Collection<WmPage> contents() {
        return pages;
    }
}
