package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.ImageCategory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class EventService extends InternalService<Event, Long> {

    public EventService(EventRepository repository) {
        super(repository);
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

    private void removeNonConformingRevs(Event event) {
        event
                .getRevisions()
                .stream()
                .filter(r -> !(
                        isWithinDateRange(r.getTimestamp(), event.getStartDate(), event.getEndDate()) &&
                        event.getProjects().contains(r.getProject()) &&
                        event.getParticipants().contains(r.getUser())
                ))
                .forEach(event::removeRevision);
        event
                .getImages()
                .stream()
                .filter(i -> !(
                        isWithinDateRange(i.getTimestamp(), event.getStartDate(), event.getEndDate()) &&
                        i.getCategories().contains(new ImageCategory(event.getCategory()))
                        ))
                .forEach(event::removeImage);
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
