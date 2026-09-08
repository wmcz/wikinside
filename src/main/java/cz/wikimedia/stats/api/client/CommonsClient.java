package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.UserAgent;
import org.springframework.stereotype.Component;

@Component
public class CommonsClient extends WmClient {
    public CommonsClient(UserAgent userAgent) {
        super("commons.wikimedia.org", userAgent.value());
    }


}
