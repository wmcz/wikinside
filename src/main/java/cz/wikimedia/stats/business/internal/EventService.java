package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Impact;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService extends InternalService<Event, Long> {
    private final EventRepository eventRepository;
    private final ImpactService impactService;

    public EventService(EventRepository repository, ImpactService impactService) {
        super(repository);
        this.eventRepository = repository;
        this.impactService = impactService;
    }

    private boolean isWithinDateRange(Instant timestamp, LocalDate start, LocalDate end) {
        return timestamp.isAfter(
                start
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()) &&
                timestamp.isBefore(
                        end
                                .plusDays(1)
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant());
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

    public void removeNonConformingRevs(Event event) {
        event
                .getRevisions()
                .stream()
                .filter(r -> !(
                        isWithinDateRange(r.getTimestamp(), event.getStartDate(), event.getEndDate()) &&
                        event.getProjects().contains(r.getProject()) &&
                        event.getParticipants().contains(r.getUser())
                ))
                .toList()
                .forEach(event::removeRevision);

        if (event.getHashtag() != null)
            // for automatically generated users, remove them if they have no revisions
            event
                    .getParticipants()
                    .retainAll(event
                            .getRevisions()
                            .stream()
                            .map(Revision::getUser)
                            .collect(Collectors.toSet()));
    }

    @Override
    public <S extends Event> Optional<S> update(S event) {
        Optional<Event> current = findById(event.getId());

        if (current.isEmpty()) return Optional.empty();
        else {
            removeNonConformingRevs(event);
            return super.update(event);
        }
    }
}
