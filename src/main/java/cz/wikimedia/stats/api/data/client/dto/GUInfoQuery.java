package cz.wikimedia.stats.api.data.client.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record GUInfoQuery(@JsonProperty("globaluserinfo") GlobalUserInfo globalUserInfo) implements Query<GlobalUserInfo> {}
