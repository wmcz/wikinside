package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record UserContribQuery(@JsonProperty("usercontribs") Collection<UserContrib> userContribs) implements Query<Collection<UserContrib>> {
    @Override
    public Collection<UserContrib> contents() {
        return userContribs;
    }
}
