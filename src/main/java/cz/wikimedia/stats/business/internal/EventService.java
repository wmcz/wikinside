package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Impact;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class EventService extends InternalService<Event, Long> {
    private final EventRepository eventRepository;
    private final ImpactService impactService;

    public EventService(EventRepository repository, ImpactService impactService) {
        super(repository);
        this.eventRepository = repository;
        this.impactService = impactService;
    }

    public Collection<Event> findByName(String name) {
        return eventRepository.findEventsByName(name);
    }

    public Impact getImpact(Event event) {
        return impactService.getImpact(new HashSet<>(event.getRevisions()));
    }

    public Impact getImpact(Collection<Event> events) {
        return impactService.getImpact(
                events
                        .stream()
                        .map(Event::getRevisions)
                        .reduce(new HashSet<>(),
                                (acc, revs) -> {acc.addAll(revs); return acc;},
                                (acc, acc2) -> {acc.addAll(acc2); return acc;}));
    }
}
