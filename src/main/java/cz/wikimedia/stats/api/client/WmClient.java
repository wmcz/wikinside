package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.api.client.dto.BatchResponse;
import cz.wikimedia.stats.api.client.dto.GUInfoQuery;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class WmClient {
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
}
