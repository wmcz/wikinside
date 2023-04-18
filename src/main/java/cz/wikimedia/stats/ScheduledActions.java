package cz.wikimedia.stats;

import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.RevisionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ScheduledActions {
    private final EventService eventService;
    private final RevisionService revisionService;

    public ScheduledActions(EventService eventService, RevisionService revisionService) {
        this.eventService = eventService;
        this.revisionService = revisionService;
    }

    @Scheduled(cron = "0 13 4 * * *")
    public void updateEvents() {
        eventService.findAll().spliterator().forEachRemaining(e -> {
            if (e.getEndDate().isBefore(LocalDate.now().minusDays(3))) {
                revisionService.asyncGenerateRevs(e.getId());
            }
        });
    }
}
