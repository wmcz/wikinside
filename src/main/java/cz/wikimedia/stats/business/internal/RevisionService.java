package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.business.external.WmRevisionService;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Revision;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RevisionService extends InternalService<Revision, Long> {
    private final WmRevisionService wmRevisionService;
    private final WmUserService wmUserService;

    public RevisionService(CrudRepository<Revision, Long> repository, WmRevisionService wmRevisionService, WmUserService wmUserService) {
        super(repository);
        this.wmRevisionService = wmRevisionService;
        this.wmUserService = wmUserService;
    }

    public Collection<Revision> getPossible(Event event) {
        return wmRevisionService.getUserContribs(
                event.getParticipants().stream().map(u -> wmUserService.getUsername(u.getId())).toList(),
                event.getStartDate(),
                event.getEndDate(),
                event.getProjects());
    }
}
