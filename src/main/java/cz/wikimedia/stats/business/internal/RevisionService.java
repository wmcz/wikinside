package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.business.external.HashtagsService;
import cz.wikimedia.stats.business.external.WmRevisionService;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.dao.RevisionRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Revision;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class RevisionService extends InternalService<Revision, Long> {
    private final WmRevisionService wmRevisionService;
    private final WmUserService wmUserService;
    private final RevisionRepository revisionRepository;
    private final HashtagsService hashtagsService;

    public RevisionService(CrudRepository<Revision, Long> repository, WmRevisionService wmRevisionService, WmUserService wmUserService, RevisionRepository revisionRepository, HashtagsService hashtagsService) {
        super(repository);
        this.wmRevisionService = wmRevisionService;
        this.wmUserService = wmUserService;
        this.revisionRepository = revisionRepository;
        this.hashtagsService = hashtagsService;
    }

    private Collection<Revision> saveAndReturn(Collection<Revision> revs) {
        return revs
                .stream()
                .map(r -> revisionRepository
                        .findRevisionByRevIdAndProject(r.getRevId(), r.getProject())
                        .orElse(revisionRepository.save(r))) // save if not already present
                .toList();
    }

    public Collection<Revision> generateFromUserList(Event event) {
        wmUserService.updateNames(event.getParticipants());
        Collection<Revision> revs = wmRevisionService.getUserContribs(event).stream().map(r -> r.setEvent(event)).toList();
        return saveAndReturn(revs);
    }

    public Collection<Revision> generateFromHashTags(Event event) {
        return saveAndReturn(hashtagsService.getRevisions(event));
    }

    public Collection<Revision> updateFromHashTags(Event event) {
        LocalDate start = revisionRepository
                .findTopRevisionByEventOrderByTimestampDesc(event)
                .map(r -> LocalDate.ofInstant(r.getTimestamp(), ZoneId.systemDefault()))
                .orElse(event.getStartDate());
        return saveAndReturn(hashtagsService.getRevisions(event, start, event.getEndDate()));
    }
}
