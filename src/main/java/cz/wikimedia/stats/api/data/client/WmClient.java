package cz.wikimedia.stats.api.data.client;

import cz.wikimedia.stats.api.data.client.dto.BatchResponse;
import cz.wikimedia.stats.api.data.client.dto.GlobalUserInfo;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class WmClient {
    private final WebClient client;

    public WmClient(String projectUrl, BuildProperties properties) {
        this.client = WebClient
                .builder()
                .defaultHeader("User-Agent", properties.getName() + "/" + properties.getVersion() + " (" + properties.get("contact") + ")")
                .baseUrl(projectUrl + "/w/api.php")
                .build();

    }

    public BatchResponse<GlobalUserInfo> getGlobalUserInfo(String username) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "query")
                        .queryParam("format", "json")
                        .queryParam("meta", "globalUserInfo")
                        .queryParam("formatVersion", 2)
                        .queryParam("guiUser", username)
                        .queryParam("maxLag", 1)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BatchResponse<GlobalUserInfo>>() {})
                .block();
    }

    public BatchResponse<GlobalUserInfo> getGlobalUserInfo(Long globalUserId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "query")
                        .queryParam("format", "json")
                        .queryParam("meta", "globalUserInfo")
                        .queryParam("formatVersion", 2)
                        .queryParam("guiid", globalUserId)
                        .queryParam("maxLag", 1)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BatchResponse<GlobalUserInfo>>() {})
                .block();
    }
}
