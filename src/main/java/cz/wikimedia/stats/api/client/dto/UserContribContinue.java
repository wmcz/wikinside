package cz.wikimedia.stats.api.client.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record UserContribContinue(@JsonProperty("uccontinue") String ucContinue) implements Continue<Collection<UserContrib>> {
    @Override
    public String listContinue() {
        return ucContinue;
    }
}
