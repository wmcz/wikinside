package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.business.external.HashtagsService;
import cz.wikimedia.stats.business.external.WmCommonsService;
import cz.wikimedia.stats.business.external.WmRevisionService;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.model.Event;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Service
public class RetrievalService {

    private final WmUserService wmUserService;
    private final RevisionService revisionService;
    private final WmRevisionService wmRevisionService;
    private final EventService eventService;
    private final HashtagsService hashtagsService;
    private final ImageService imageService;
    private final WmCommonsService wmCommonsService;

    public RetrievalService(WmUserService wmUserService, RevisionService revisionService, WmRevisionService wmRevisionService, EventService eventService, HashtagsService hashtagsService, ImageService imageService, WmCommonsService wmCommonsService) {
        this.wmUserService = wmUserService;
        this.revisionService = revisionService;
        this.wmRevisionService = wmRevisionService;
        this.eventService = eventService;
        this.hashtagsService = hashtagsService;
        this.imageService = imageService;
        this.wmCommonsService = wmCommonsService;
    }

    private void retrieveFromUserList(Event event) {
        wmUserService.updateNames(event.getParticipants());
        revisionService.updateOrCreate(wmRevisionService.getUserContribs(event, event.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()).stream().map(r -> r.addEvent(event)).toList());
        eventService.update(event);
    }

    private void retrieveFromHashTags(Event event) {
        revisionService.updateOrCreate(hashtagsService.getRevisions(event, event.getStartDate(), event.getEndDate()));
    }

    private void retrieveFromPhotoCategory(Event event) {
        imageService.updateOrCreate(wmCommonsService.getImages(event));
    }
    public void retrieve(Event event) {
        switch (event.getStrategy()) {
            case MANUAL  -> retrieveFromUserList(event);
            case HASHTAG -> retrieveFromHashTags(event);
            case PHOTO   -> retrieveFromPhotoCategory(event);
        }
    }

    @Async
    @Transactional
    public void asyncRetrieve(Long eventId) {
        eventService.findById(eventId).ifPresent(this::retrieve);
    }

}
