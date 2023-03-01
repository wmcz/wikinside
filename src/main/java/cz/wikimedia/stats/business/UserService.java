package cz.wikimedia.stats.business;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.UserRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserService extends AbstractService<User, Long> {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public UserService(UserRepository repository, EventRepository eventRepository) {
        super(repository);
        this.userRepository = repository;
        this.eventRepository = eventRepository;
    }

    private Collection<Event> applyEvents(User user, Collection<Long> eventIds, Function<Event, Event> function) {
        return applyToNonOwningField(user, eventIds, function, eventRepository, userRepository, User::getEvents);
    }

    private void updateEvents(User user, Collection<Event> updated) {
        updateNonOwningField(user, updated, User::getEvents, this::addEvents, this::removeEvents);
    }

    public Collection<User> findByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    public Collection<Event> addEvents(User user, Collection<Long> eventIds) {
        return applyEvents(user, eventIds, e -> e.addParticipant(user));
    }

    public Collection<Event> removeEvents(User user, Collection<Long> eventIds) {
        return applyEvents(user, eventIds, e -> e.removeParticipant(user));
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
