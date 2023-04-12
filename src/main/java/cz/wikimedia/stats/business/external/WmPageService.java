package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import cz.wikimedia.stats.api.client.dto.WmPage;
import cz.wikimedia.stats.model.Project;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class WmPageService {
    private final WmClientService wmClientService;

    public WmPageService(WmClientService wmClientService) {
        this.wmClientService = wmClientService;
    }

    private Collection<WmPage> getPagesInner(Collection<Long> revIds, WmClient client) {
        return client.getRevInfo(ClientUtils.collect(revIds)).query().contents();

    }

    public Collection<WmPage> getPages(Collection<Long> revIds, Project project) {
        WmClient client = wmClientService.getClient(project);
        Collection<WmPage> result = new ArrayList<>(revIds.size());

        while (revIds.size() > 0) {
            result.addAll(getPagesInner(revIds.stream().limit(50).toList(), client));
            revIds = revIds.stream().skip(50).toList();
        }
        return result;
    }
}
