package cz.wikimedia.stats.api.data.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GlobalUserInfo(@JsonProperty("home") String home,
                             @JsonProperty("id") Long id,
                             @JsonProperty("registration") Instant registration,
                             @JsonProperty("name") String name) {}
