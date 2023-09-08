package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.model.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Service
public class ImpactService {
    private final DiffStrategy strategy = DiffStrategy.ABSOLUTE;
        enum DiffStrategy {
            NET,
            POSITIVE,
            ABSOLUTE
        }

        private Function<Revision, Long> getDiffFunc() {
            return switch (strategy) {
                case NET      -> Revision::getDiff;
                case ABSOLUTE -> r -> Math.abs(r.getDiff());
                case POSITIVE -> r -> (r.getDiff() > 0 ? r.getDiff() : 0);
            };
        }

        private Long getDiff(Set<Revision> revs) {
            return revs.stream().map(getDiffFunc()).reduce(0L, Long::sum);
        }

        private Long getCreatedPages(Set<Revision> revs) {
            return revs.stream().filter(Revision::isPageCreation).count();
        }

        private Long getEditedPages(Set<Revision> revs) {
            Set<Page> pages = new HashSet<>();
            revs.forEach(r -> pages.add(new Page(r.getProject(), r.getPageId())));
            return Long.valueOf(pages.size());
        }

        private Long getUsers(Set<Revision> revs) {
            Set<User> users = new HashSet<>();
            revs.forEach(r -> users.add(r.getUser()));
            return Long.valueOf(users.size());
        }
        public Impact getImpact(Set<Revision> revs) {

            return new Impact(getCreatedPages(revs),
                              getEditedPages(revs),
                              Long.valueOf(revs.size()),
                              getDiff(revs),
                              getUsers(revs));
        }

        public Impact getImpact(Event event) {
            return getImpact(event.getRevisions());
        }

        private void addRevs(EventTag tag, Set<Revision> revs) {
            revs.addAll(tag.getTagged().stream().flatMap(e -> e.getRevisions().stream()).toList());
            tag.getChildren().forEach(t -> addRevs(t, revs));
        }

        public Impact getImpact(EventTag tag) {
            Set<Revision> revs = new HashSet<>();
            addRevs(tag, revs);
            return getImpact(revs);
        }
}
