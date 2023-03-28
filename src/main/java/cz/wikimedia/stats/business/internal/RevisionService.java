package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.business.external.HashtagsService;
import cz.wikimedia.stats.business.external.WmRevisionService;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.RevisionRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Revision;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Comparator;
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

    private Collection<Revision> saveAndReturn(Collection<Revision> revs) {
        return revs
                .stream()
                .map(r -> revisionRepository
                        .findRevisionByRevIdAndProject(r.getRevId(), r.getProject())
                        .map(f -> {r.getEvents().forEach(e -> eventService.update(e.addRevision(f))); return repository.findById(f.getId()).get();})
                        .orElse(create(r).get())) // save if not already present
                .toList();
    }

    public Collection<Revision> generateFromUserList(Event event) {
        wmUserService.updateNames(event.getParticipants());
        Collection<Revision> revs = wmRevisionService.getUserContribs(event).stream().map(r -> r.addEvent(event)).toList();
        return saveAndReturn(revs);
    }

    public Collection<Revision> generateFromHashTags(Event event) {
        return saveAndReturn(hashtagsService.getRevisions(event));
    }

    public Collection<Revision> updateFromHashTags(Event event) {
        LocalDate start = event.getRevisions().stream().max(Comparator.comparing(Revision::getTimestamp))
                .map(r -> LocalDate.ofInstant(r.getTimestamp(), ZoneId.systemDefault()))
                .orElse(event.getStartDate());
        return saveAndReturn(hashtagsService.getRevisions(event, start, event.getEndDate()));
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
