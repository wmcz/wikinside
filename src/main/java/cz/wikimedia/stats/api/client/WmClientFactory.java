package cz.wikimedia.stats.api.client;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class WmClientFactory {
    private final BuildProperties properties;

    public WmClientFactory(BuildProperties properties) {
        this.properties = properties;
    }

    public WmClient create(String projectUrl) {
        return new WmClient(projectUrl, properties);
    }
}
