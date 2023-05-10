package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record WmPage(@JsonProperty("pageid") Long pageId,
                     @JsonProperty("ns") int namespace,
                     @JsonProperty("title") String title,
                     @JsonProperty("revisions") Collection<WmRev> revs) {}
