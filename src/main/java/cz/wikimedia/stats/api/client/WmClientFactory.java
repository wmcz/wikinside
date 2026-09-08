package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.UserAgent;
import org.springframework.stereotype.Component;

@Component
public class WmClientFactory {
    private final UserAgent userAgent;

    public WmClientFactory(UserAgent userAgent) {
        this.userAgent = userAgent;
    }

    public WmClient create(String projectUrl) {
        return new WmClient(projectUrl, userAgent.value());
    }
}
