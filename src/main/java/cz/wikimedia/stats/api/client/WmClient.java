package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.api.client.dto.*;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

public class WmClient {
    private final WebClient client;

    public WmClient(String projectUrl, BuildProperties properties) {
        this.client = WebClient
                .builder()
                .defaultHeader("User-Agent", properties.getName() + "/" + properties.getVersion() + " (" + properties.get("contact") + ")")
                .baseUrl("https://" + projectUrl + "/w/api.php")
                .build();
    }

    public BatchResponse<GUInfoQuery> getGlobalUserInfo(String username) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "query")
                        .queryParam("format", "json")
                        .queryParam("meta", "globaluserinfo")
                        .queryParam("formatversion", 2)
                        .queryParam("guiuser", username)
                        .queryParam("maxlag", 1)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BatchResponse<GUInfoQuery>>() {})
                .block();
    }

    public BatchResponse<GUInfoQuery> getGlobalUserInfo(Long globalUserId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "query")
                        .queryParam("format", "json")
                        .queryParam("meta", "globaluserinfo")
                        .queryParam("formatversion", 2)
                        .queryParam("guiid", globalUserId)
                        .queryParam("maxlag", 1)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BatchResponse<GUInfoQuery>>() {})
                .block();
    }


    public ContinuableBatchResponse<UserContribQuery, UserContribContinue> getUserContribs(String names, Instant start, Instant end) {

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "query")
                        .queryParam("format", "json")
                        .queryParam("list", "usercontribs")
                        .queryParam("formatversion", 2)
                        .queryParam("ucuser", names)
                        .queryParam("ucend", start)
                        .queryParam("ucstart", end)
                        .queryParam("ucprop", "ids|title|timestamp|comment|sizediff|flags")
                        .queryParam("maxlag", 1)
                        .queryParam("uclimit", 200)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ContinuableBatchResponse<UserContribQuery, UserContribContinue>>() {})
                .block();
    }

    public ContinuableBatchResponse<UserContribQuery, UserContribContinue> getMoreUserContribs(String names, Instant start, Instant end, String ucContinue) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "query")
                        .queryParam("format", "json")
                        .queryParam("list", "usercontribs")
                        .queryParam("formatversion", 2)
                        .queryParam("ucuser", names)
                        .queryParam("ucend", start)
                        .queryParam("ucstart", end)
                        .queryParam("ucprop", "ids|title|timestamp|comment|sizediff|flags")
                        .queryParam("uccontinue", ucContinue)
                        .queryParam("maxlag", 1)
                        .queryParam("uclimit", 200)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ContinuableBatchResponse<UserContribQuery, UserContribContinue>>() {})
                .block();
    }
}
