package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record HashtagsInfo(@JsonProperty("Domain") String domain,
                           @JsonProperty("Timestamp") Instant timestamp,
                           @JsonProperty("Username") String username,
                           @JsonProperty("Page_title") String pageTitle,
                           @JsonProperty("Edit_summary") String summary,
                           @JsonProperty("Revision_ID") Long revId) {
}
