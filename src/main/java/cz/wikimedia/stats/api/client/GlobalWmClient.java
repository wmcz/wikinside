package cz.wikimedia.stats.api.client;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class GlobalWmClient extends WmClient {
    // for things that do not need a particular project
    // use WmClient and let Spring autowire
    public GlobalWmClient(BuildProperties properties) {
        super("meta.wikimedia.org", properties);
    }


}
