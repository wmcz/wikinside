package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.UserAgent;
import cz.wikimedia.stats.api.client.dto.HashtagsQuery;
import cz.wikimedia.stats.model.Project;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Component
public class HashtagsClient extends AbstractClient {
    public HashtagsClient(UserAgent userAgent) {
        super(WebClient
                .builder()
                .defaultHeader("User-Agent", userAgent.value())
                .baseUrl("https://hashtags.wmcloud.org/json/")
                .build());
    }

    public HashtagsQuery getFromHashtag(String hashtag, Project project, LocalDate startDate, LocalDate endDate) {
        return getWithRetries(
                uriBuilder -> uriBuilder
                        .queryParam("query", hashtag)
                        .queryParam("project", project.getPath())
                        .queryParam("startdate", startDate)
                        .queryParam("enddate", endDate)
                        .build(),
                new ParameterizedTypeReference<HashtagsQuery>() {});
    }
}
