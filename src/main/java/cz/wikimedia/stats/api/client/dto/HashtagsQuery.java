package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record HashtagsQuery(@JsonProperty("Rows") Collection<HashtagsInfo> rows) {
}
