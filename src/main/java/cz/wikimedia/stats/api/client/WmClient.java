package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.api.client.dto.*;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.time.Instant;

public class WmClient {
    private final WebClient client;

    private UriBuilder getdefaultQueryParams(UriBuilder uriBuilder) {
        return uriBuilder
                .queryParam("action",        "query")
                .queryParam("format",        "json")
                .queryParam("formatversion", 2)
                .queryParam("maxlag",        3);
    }

    public WmClient(String projectUrl, BuildProperties properties) {
        this.client = WebClient
                .builder()
                .defaultHeader("User-Agent", properties.getName() + "/" + properties.getVersion() + " (" + properties.get("contact") + ")")
                .baseUrl("https://" + projectUrl + "/w/api.php")
                .build();
    }

    public BatchResponse<GUInfoQuery> getGlobalUserInfo(String username) {
        return client.get()
                .uri(uriBuilder -> getdefaultQueryParams(uriBuilder)
                        .queryParam("meta", "globaluserinfo")
                        .queryParam("guiuser", username)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BatchResponse<GUInfoQuery>>() {})
                .block();
    }

    public BatchResponse<GUInfoQuery> getGlobalUserInfo(Long globalUserId) {
        return client.get()
                .uri(uriBuilder -> getdefaultQueryParams(uriBuilder)
                        .queryParam("meta", "globaluserinfo")
                        .queryParam("guiid", globalUserId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BatchResponse<GUInfoQuery>>() {})
                .block();
    }

    public BatchResponse<LocalUserQuery> getUsersById(String localIds) {
        return client.get()
                .uri(uriBuilder -> getdefaultQueryParams(uriBuilder)
                        .queryParam("list", "users")
                        .queryParam("ususerids", localIds)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BatchResponse<LocalUserQuery>>() {})
                .block();
    }

    public BatchResponse<LocalUserQuery> getUsersByName(String names) {
        return client.get()
                .uri(uriBuilder -> getdefaultQueryParams(uriBuilder)
                        .queryParam("list", "users")
                        .queryParam("ususers", names)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BatchResponse<LocalUserQuery>>() {})
                .block();
    }


    public ContinuableBatchResponse<UserContribQuery, UserContribContinue> getUserContribs(String names, Instant start, Instant end) {

        return client.get()
                .uri(uriBuilder -> getdefaultQueryParams(uriBuilder)
                        .queryParam("list", "usercontribs")
                        .queryParam("ucuser", names)
                        .queryParam("ucend", start)
                        .queryParam("ucstart", end)
                        .queryParam("ucprop", "ids|title|timestamp|comment|sizediff|flags")
                        .queryParam("uclimit", 200)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ContinuableBatchResponse<UserContribQuery, UserContribContinue>>() {})
                .block();
    }

    public ContinuableBatchResponse<UserContribQuery, UserContribContinue> getMoreUserContribs(String names, Instant start, Instant end, String ucContinue) {
        return client.get()
                .uri(uriBuilder -> getdefaultQueryParams(uriBuilder)
                        .queryParam("list", "usercontribs")
                        .queryParam("ucuser", names)
                        .queryParam("ucend", start)
                        .queryParam("ucstart", end)
                        .queryParam("ucprop", "ids|title|timestamp|comment|sizediff|flags")
                        .queryParam("uccontinue", ucContinue)
                        .queryParam("uclimit", 200)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ContinuableBatchResponse<UserContribQuery, UserContribContinue>>() {})
                .block();
    }
}
