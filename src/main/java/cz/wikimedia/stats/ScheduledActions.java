package cz.wikimedia.stats;

import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.ImageService;
import cz.wikimedia.stats.business.internal.RetrievalService;
import cz.wikimedia.stats.business.internal.RevisionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ScheduledActions {
    private final EventService eventService;
    private final RevisionService revisionService;
    private final ImageService imageService;
    private final RetrievalService retrievalService;

    public ScheduledActions(EventService eventService, RevisionService revisionService, ImageService imageService, RetrievalService retrievalService) {
        this.eventService = eventService;
        this.revisionService = revisionService;
        this.imageService = imageService;
        this.retrievalService = retrievalService;
    }

    @Scheduled(cron = "0 13 4 * * *")
    public void updateEvents() {
        eventService.findAll().spliterator().forEachRemaining(e -> {
            if (e.getEndDate().isAfter(LocalDate.now().minusDays(3))) {
                retrievalService.asyncRetrieve(e.getId());
            }
        });
    }

    @Scheduled(cron = "0 16 4 * * *")
    public void removeUnused() {
        revisionService.findAll().spliterator().forEachRemaining(r -> {
            if (r.getEvents().isEmpty()) imageService.deleteById(r.getId());
        });
    }
}
