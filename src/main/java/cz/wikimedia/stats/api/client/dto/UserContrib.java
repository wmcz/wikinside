package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserContrib(@JsonProperty("userid") Long userId,
                          @JsonProperty("user") String user,
                          @JsonProperty("revid") Long revId,
                          @JsonProperty("title") String title,
                          @JsonProperty("timestamp") Instant timestamp,
                          @JsonProperty("comment") String comment,
                          @JsonProperty("sizediff") Long sizeDiff) {
}
