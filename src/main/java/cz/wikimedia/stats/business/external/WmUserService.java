package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.WmClient;
import cz.wikimedia.stats.api.client.dto.GlobalUserInfo;
import cz.wikimedia.stats.api.client.dto.LocalUserInfo;
import cz.wikimedia.stats.dao.UserRepository;
import cz.wikimedia.stats.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class WmUserService extends WmService {

    private final UserRepository userRepository;

    protected WmUserService(WmClient client, UserRepository userRepository) {
        super(client);
        this.userRepository = userRepository;
    }

    public String getUsername(Long globalUserId) {
        return client.getGlobalUserInfo(globalUserId).query().contents().name();
    }

    public GlobalUserInfo getGlobalUserInfo(String username) {
        return client.getGlobalUserInfo(username).query().contents();
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
        try {
            Collection<LocalUserInfo> updated = ClientUtils.applyWithLimit(
                    users.stream().map(User::getLocalId).toList(),
                    ids -> client.getUsersById(ClientUtils.collect(ids)).query().contents());

            users
                    .stream()
                    .filter(u -> u.getLocalId() != null)
                    .forEach(u -> {
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
            userRepository.saveAll(users);

        } catch (WebClientException ignored) {} // it's okay if the usernames don't update if the connection can't be made

        return users;
    }
}
