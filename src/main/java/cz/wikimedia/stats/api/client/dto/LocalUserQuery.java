package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record LocalUserQuery(@JsonProperty("users") Collection<LocalUserInfo> users) implements Query<Collection<LocalUserInfo>> {
    @Override
    public Collection<LocalUserInfo> contents() {
        return users;
    }
}
