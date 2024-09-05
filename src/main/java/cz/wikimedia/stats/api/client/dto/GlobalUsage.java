package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GlobalUsage(@JsonProperty("title") String title,
                          @JsonProperty("wiki") String project,
                          @JsonProperty("url") String url) {}
