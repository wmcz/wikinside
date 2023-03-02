package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;

public abstract class WmService {
    protected final WmClient client;

    protected WmService(WmClient client) {
        this.client = client;
    }
}
