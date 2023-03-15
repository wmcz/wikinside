package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocalUserInfo(@JsonProperty("userid") Long userid,
                            @JsonProperty("name") String name) {
}
