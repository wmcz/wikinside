package cz.wikimedia.stats.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record RevisionDto(@JsonProperty("id") Long id,
                          @JsonProperty("revId") Long revId,
                          @JsonProperty("diff") Long diff,
                          @JsonProperty("userId") Long userId,
                          @JsonProperty("eventId") Long eventId,
                          @JsonProperty("projectId") Long projectId,
                          @JsonProperty("timestamp") Instant timestamp,
                          @JsonProperty("summary") String summary) {}
