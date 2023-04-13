package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.business.external.HashtagsService;
import cz.wikimedia.stats.business.external.WmRevisionService;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.RevisionRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Revision;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

@Service
public class RevisionService extends InternalService<Revision, Long> {
    private final WmRevisionService wmRevisionService;
    private final WmUserService wmUserService;
    private final RevisionRepository revisionRepository;
    private final HashtagsService hashtagsService;
    private final EventRepository eventRepository;
    private final EventService eventService;

    public RevisionService(CrudRepository<Revision, Long> repository, WmRevisionService wmRevisionService, WmUserService wmUserService, RevisionRepository revisionRepository, HashtagsService hashtagsService, EventService eventService, EventRepository eventRepository) {
        super(repository);
        this.wmRevisionService = wmRevisionService;
        this.wmUserService = wmUserService;
        this.revisionRepository = revisionRepository;
        this.hashtagsService = hashtagsService;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    private Collection<Event> applyEvents(Revision rev, Collection<Long> eventIds, Function<Event, Event> function) {
        return applyToNonOwningField(rev, eventIds, function, eventRepository, revisionRepository, Revision::getEvents);
    }

    private Collection<Event> addEvents(Revision rev, Collection<Long> eventIds) {
        return applyEvents(rev, eventIds, e -> e.addRevision(rev));
    }

    private Collection<Event> removeEvents(Revision rev, Collection<Long> eventIds) {
        return applyEvents(rev, eventIds, e -> e.removeRevision(rev));
    }

    private void updateEvents(Revision rev, Collection<Event> updated) {
        updateNonOwningField(rev, updated, Revision::getEvents, this::addEvents, this::removeEvents);
    }

    private Collection<Revision> getEventRevisions(Event event) {
        return eventService
                .findById(event.getId())
                .map(Event::getRevisions)
                .orElse(Collections.emptySet());
    }

    private Revision updateOrCreate(Revision rev) {
        System.out.println(rev.getRevId());
        Optional<Revision> original = revisionRepository.findRevisionByRevIdAndProject(rev.getRevId(), rev.getProject());

        if (original.isPresent()) {
            rev.getEvents().forEach(e -> original.get().addEvent(e));
            return update(original.get()).orElse(null);

        } else return create(rev).orElse(null);
    }

    private Collection<Revision> updateOrCreate(Collection<Revision> revs) {
        return revs
                .stream()
                .map(this::updateOrCreate) // save if not already present
                .toList();
    }

    public Collection<Revision> generateFromUserList(Event event) {
        wmUserService.updateNames(event.getParticipants());
        updateOrCreate(wmRevisionService.getUserContribs(event, event.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()).stream().map(r -> r.addEvent(event)).toList());
        return getEventRevisions(event);
    }

    public Collection<Revision> generateFromHashTags(Event event) {
        updateOrCreate(hashtagsService.getRevisions(event, event.getStartDate(), event.getEndDate()));
        return getEventRevisions(event);
    }

    private Instant getLastTimeStamp(Event event) {
        return event
                .getRevisions()
                .stream()
                .map(Revision::getTimestamp)
                .max(Instant::compareTo)
                .orElse(event
                        .getStartDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());
    }

    public Collection<Revision> generateRevs(Event event) {
        if (event.getHashtag() == null)
             return generateFromUserList(event);
        else return generateFromHashTags(event);
    }

    @Async
    @Transactional
    public void asyncGenerateRevs(Event event) {
        generateRevs(event);
    }

    @Override
    public <S extends Revision> Optional<S> create(S rev) {
        Optional<S> res = super.create(rev);
        if (res.isPresent()) {
            addEvents(res.get(), toIds(rev.getEvents()));
            return update(res.get());
        }
        else return res;
    }

    @Override
    public <S extends Revision> Optional<S> update(S rev) {
        Optional<Revision> current = findById(rev.getId());

        if (current.isEmpty()) return Optional.empty();
        else {
            if (rev.getEvents().size() == 0) {
                deleteById(rev.getId());
                return Optional.of(rev);
            }
            updateEvents(current.get(), rev.getEvents());
            return super.update(rev);
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(r -> removeEvents(r, toIds(r.getEvents())));
        super.deleteById(id);
    }

}
