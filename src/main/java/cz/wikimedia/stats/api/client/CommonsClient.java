package cz.wikimedia.stats.api.client;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class CommonsClient extends WmClient {
    public CommonsClient(BuildProperties properties) {
        super("commons.wikimedia.org", properties);
    }


}
