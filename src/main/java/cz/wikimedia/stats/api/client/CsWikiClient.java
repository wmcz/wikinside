package cz.wikimedia.stats.api.client;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class CsWikiClient extends WmClient {
    public CsWikiClient(BuildProperties properties) {
        super("cs.wikipedia.org", properties);
    }
}
