package cz.wikimedia.stats.api.client;

import cz.wikimedia.stats.UserAgent;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class GlobalWmClient extends WmClient {
    // for things that do not need a particular project
    // use WmClient and let Spring autowire
    public GlobalWmClient(UserAgent userAgent) {
        super("meta.wikimedia.org", userAgent.value());
    }


}
