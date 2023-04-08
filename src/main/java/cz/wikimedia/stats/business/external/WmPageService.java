package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import cz.wikimedia.stats.api.client.dto.WmPage;
import cz.wikimedia.stats.model.Project;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class WmPageService {
    private final WmClientService wmClientService;

    public WmPageService(WmClientService wmClientService) {
        this.wmClientService = wmClientService;
    }

    public Collection<WmPage> getPages(Collection<String> titles, Project project) {
        WmClient client = wmClientService.getClient(project);
        return client.getPageInfo(ClientUtils.collectNames(titles)).query().contents();
    }
}
