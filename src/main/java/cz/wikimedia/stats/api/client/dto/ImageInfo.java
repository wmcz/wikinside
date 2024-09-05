package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ImageInfo(@JsonProperty("timestamp") Instant timestamp,
                        @JsonProperty("user") String username) {
}
