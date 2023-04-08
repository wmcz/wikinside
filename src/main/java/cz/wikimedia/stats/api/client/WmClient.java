package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.api.client.dto.*;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.time.Instant;
import java.util.function.Function;

public class WmClient {
    private final WebClient client;

    private UriBuilder getdefaultQueryParams(UriBuilder uriBuilder) {
        return uriBuilder
                .queryParam("action",        "query")
                .queryParam("format",        "json")
                .queryParam("formatversion", 2)
                .queryParam("maxlag",        3);
    }

    private <T> T get(Function<UriBuilder, UriBuilder> params) {
        return client
                .get()
                .uri(uriBuilder -> params.apply(getdefaultQueryParams(uriBuilder)).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<T>() {})
                .block();
    }

    public WmClient(String projectUrl, BuildProperties properties) {
        this.client = WebClient
                .builder()
                .defaultHeader("User-Agent", properties.getName() + "/" + properties.getVersion() + " (" + properties.get("contact") + ")")
                .baseUrl("https://" + projectUrl + "/w/api.php")
                .build();
    }

    public BatchResponse<GUInfoQuery> getGlobalUserInfo(String username) {
        return get(uriBuilder -> uriBuilder
                .queryParam("meta", "globaluserinfo")
                .queryParam("guiuser", username));
    }

    public BatchResponse<GUInfoQuery> getGlobalUserInfo(Long globalUserId) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("meta", "globaluserinfo")
                        .queryParam("guiid", globalUserId));
    }

    public BatchResponse<LocalUserQuery> getUsersById(String localIds) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("list", "users")
                        .queryParam("ususerids", localIds));
    }

    public BatchResponse<LocalUserQuery> getUsersByName(String names) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("list", "users")
                        .queryParam("ususers", names));
    }


    public ContinuableBatchResponse<UserContribQuery, UserContribContinue> getUserContribs(String names, Instant start, Instant end) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("list", "usercontribs")
                        .queryParam("ucuser", names)
                        .queryParam("ucend", start)
                        .queryParam("ucstart", end)
                        .queryParam("ucprop", "ids|title|timestamp|comment|sizediff|flags")
                        .queryParam("uclimit", 200));
    }

    public ContinuableBatchResponse<UserContribQuery, UserContribContinue> getMoreUserContribs(String names, Instant start, Instant end, String ucContinue) {
        return get(uriBuilder -> uriBuilder
                        .queryParam("list", "usercontribs")
                        .queryParam("ucuser", names)
                        .queryParam("ucend", start)
                        .queryParam("ucstart", end)
                        .queryParam("ucprop", "ids|title|timestamp|comment|sizediff|flags")
                        .queryParam("uccontinue", ucContinue)
                        .queryParam("uclimit", 200));
    }

    public BatchResponse<PageQuery> getPageInfo(String titles) {
        return get(uriBuilder -> uriBuilder
                .queryParam("titles", titles)
                .queryParam("prop", "revisions")
                .queryParam("rvprop", "ids")
                .queryParam("rvlimit", 1)
                .queryParam("rvdir", "newer"));
    }
}
