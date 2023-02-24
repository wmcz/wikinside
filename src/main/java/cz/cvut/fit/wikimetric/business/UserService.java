package cz.cvut.fit.wikimetric.business;

import cz.cvut.fit.wikimetric.dao.EventRepository;
import cz.cvut.fit.wikimetric.dao.UserRepository;
import cz.cvut.fit.wikimetric.model.Event;
import cz.cvut.fit.wikimetric.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

    private Collection<Event> updateEvents(User user, Collection<Long> eventIds, Function<Event, Event> function) {
        return updateNonOwningField(user, eventIds, function, eventRepository, userRepository, User::getEvents);
    }

    public Collection<User> findByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    public Collection<Event> addEvents(User user, Collection<Long> eventIds) {
        return updateEvents(user, eventIds, e -> e.addParticipant(user));
    }

    public Collection<Event> removeEvents(User user, Collection<Long> eventIds) {
        return updateEvents(user, eventIds, e -> e.removeParticipant(user));
    }
}
