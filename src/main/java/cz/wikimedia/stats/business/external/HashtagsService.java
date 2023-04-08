package cz.wikimedia.stats.business.external;

import cz.wikimedia.stats.api.client.HashtagsClient;
import cz.wikimedia.stats.api.client.dto.converter.HashtagsInfoConverter;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Component
public class HashtagsService {
    private final HashtagsClient hashtagsClient;
    private final HashtagsInfoConverter hashtagsInfoConverter;

    public HashtagsService(HashtagsClient hashtagsClient, HashtagsInfoConverter hashtagsInfoConverter) {
        this.hashtagsClient = hashtagsClient;
        this.hashtagsInfoConverter = hashtagsInfoConverter;
    }

    public Collection<Revision> getRevisions(Event event) {
       return getRevisions(event, event.getStartDate(), event.getEndDate());
    }

    public Collection<Revision> getRevisions(Event event, LocalDate start, LocalDate end) {
        Collection<Revision> revs = new HashSet<>();
        for (Project p : event.getProjects()) {
            revs.addAll(
                    hashtagsInfoConverter.fromInfo(
                            hashtagsClient.getFromHashtag(event.getHashtag(), p, start, end).rows(), p, event));
        }
        return revs;
    }
}
