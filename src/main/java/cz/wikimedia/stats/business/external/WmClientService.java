package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import cz.wikimedia.stats.api.client.WmClientFactory;
import cz.wikimedia.stats.model.Project;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WmClientService {
    private final WmClientFactory wmClientFactory;
    private final Map<String, WmClient> clients;

    public WmClientService(WmClientFactory wmClientFactory) {
        clients = new HashMap<>();
        this.wmClientFactory = wmClientFactory;
    }

    public WmClient getClient(Project project) {
        WmClient client = clients.get(project.getPath());

        if (client == null) {
            client = wmClientFactory.create(project.getPath());
            clients.put(project.getPath(), client);
        }

        return client;
    }
}
