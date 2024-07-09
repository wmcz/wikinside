package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record ImageInfoUsageQuery(@JsonProperty("pages") Collection<WmImage> images) implements Query<Collection<WmImage>> {
    @Override
    public Collection<WmImage> contents() {
        return images;
    }
}
