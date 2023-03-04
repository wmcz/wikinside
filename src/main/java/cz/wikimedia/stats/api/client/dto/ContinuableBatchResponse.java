package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContinuableBatchResponse<Q extends Query<?>, C extends Continue<?>>
        (@JsonProperty("batchcomplete") Boolean batchComplete,
         @JsonProperty("continue") C toContinue,
         @JsonProperty("query") Q query) {
}
