package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.UserRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserService extends InternalService<User, Long> {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final WmUserService wmUserService;

    public UserService(UserRepository repository, EventRepository eventRepository, WmUserService wmUserService) {
        super(repository);
        this.userRepository = repository;
        this.eventRepository = eventRepository;
        this.wmUserService = wmUserService;
    }

    private Collection<Event> applyEvents(User user, Collection<Long> eventIds, Function<Event, Event> function) {
        return applyToNonOwningField(user, eventIds, function, eventRepository, userRepository, User::getEvents);
    }

    private void updateEvents(User user, Collection<Event> updated) {
        updateNonOwningField(user, updated, User::getEvents, this::addEvents, this::removeEvents);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findById(wmUserService.getId(username));
    }

    public Collection<Event> addEvents(User user, Collection<Long> eventIds) {
        return applyEvents(user, eventIds, e -> e.addParticipant(user));
    }

    public Collection<Event> removeEvents(User user, Collection<Long> eventIds) {
        return applyEvents(user, eventIds, e -> e.removeParticipant(user));
    }

    public <S extends User> Optional<S> createFromGlobalUser(S user) {
        if (user.getId() == null)
            user.setId(wmUserService.getId(user.getUsername()));
        return create(user);
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(u -> removeEvents(u, toIds(u.getEvents())));
        super.deleteById(id);
    }

    @Override
    public <S extends User> Optional<S> create(S user) {
        Optional<S> res = super.create(user);
        if (res.isPresent()) {
            addEvents(res.get(), toIds(user.getEvents()));
            return update(res.get());

        } else return res;
    }

    @Override
    public <S extends User> Optional<S> update(S user) {
        Optional<User> current = findById(user.getId());

        if (current.isEmpty()) return Optional.empty();
        else {
            updateEvents(current.get(), user.getEvents());
            return super.update(user);
        }
    }
}
