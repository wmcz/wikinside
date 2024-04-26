package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.HashtagsClient;
import cz.wikimedia.stats.api.client.dto.converter.HashtagsInfoConverter;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Component
public class HashtagsService {
    private final HashtagsClient hashtagsClient;
    private final HashtagsInfoConverter hashtagsInfoConverter;
    private final WmRevisionService wmRevisionService;

    public HashtagsService(HashtagsClient hashtagsClient, HashtagsInfoConverter hashtagsInfoConverter, WmRevisionService wmRevisionService) {
        this.hashtagsClient = hashtagsClient;
        this.hashtagsInfoConverter = hashtagsInfoConverter;
        this.wmRevisionService = wmRevisionService;
    }

    public Collection<Revision> getRevisions(Event event) {
       return getRevisions(event, event.getStartDate(), event.getEndDate());
    }

    public Collection<Revision> getRevisions(Event event, LocalDate start, LocalDate end) {
        Collection<Revision> revs = new HashSet<>();

        for (Project p : new ArrayList<>(event.getProjects())) {
            revs.addAll(
                    wmRevisionService.addDiffs(
                            hashtagsInfoConverter.fromInfo(
                                    hashtagsClient.getFromHashtag(event.getCategory(), p, start, end).rows(), p, event),
                            p));
        }
        return revs;
    }
}
