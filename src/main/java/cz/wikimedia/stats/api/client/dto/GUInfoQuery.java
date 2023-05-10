package cz.wikimedia.stats.api.client.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record GUInfoQuery(@JsonProperty("globaluserinfo") GlobalUserInfo globalUserInfo) implements Query<GlobalUserInfo> {
    @Override
    public GlobalUserInfo contents() {
        return globalUserInfo;
    }
}
