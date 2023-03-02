package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import org.springframework.stereotype.Service;

@Service
public class WmUserService extends WmService {

    protected WmUserService(WmClient client) {
        super(client);
    }

    public String getUsername(Long globalUserId) {
        return client.getGlobalUserInfo(globalUserId).query().contents().name();
    }

    public Long getId(String username) {
        return client.getGlobalUserInfo(username).query().contents().id();
    }
}
