package cz.wikimedia.stats.api.client.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record IIUContinue(@JsonProperty("gcmcontinue") String gcmContinue) implements Continue<Collection<WmImage>> {
    @Override
    public String listContinue() {
        return gcmContinue;
    }
}
