package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import cz.wikimedia.stats.api.client.dto.LocalUserInfo;
import cz.wikimedia.stats.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

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

    public Long getLocalId(String username) {
        return client
                .getUsersByName(username)
                .query()
                .contents()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .userid();
    }

    public Collection<User> updateNames(Collection<User> users) {
        String ids = ClientUtils.collect(users.stream().map(User::getLocalId).toList());
        Collection<LocalUserInfo> updated = client.getUsersById(ids).query().contents();
        users.forEach(u -> {
            var i = updated.iterator();
            while (i.hasNext()) {
                var id = i.next();
                if (id.userid().equals(u.getLocalId())) {
                    u.setUsername(id.name());
                    i.remove();
                    break;
                }
            }
        });
        return users;
    }
}
