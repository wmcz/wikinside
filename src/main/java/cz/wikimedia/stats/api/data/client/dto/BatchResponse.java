package cz.wikimedia.stats.api.data.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BatchResponse<T>(@JsonProperty("batchcomplete") Boolean batchComplete, @JsonProperty("query") Query<T> query) {
}
