package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WmRev(@JsonProperty("revid") Long revId,
                    @JsonProperty("parentid") Long parentId,
                    @JsonProperty("size") Long size) {
}
