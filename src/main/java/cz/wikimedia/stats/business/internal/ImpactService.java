package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.model.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        private <E> Long enumerate(Set<Revision> revs, Function<Revision, E> forEach) {
            Set<E> set = new HashSet<>();
            revs.forEach(r -> set.add(forEach.apply(r)));
            return Long.valueOf(set.size());
        }

        private Long getEditedPages(Set<Revision> revs) {
            return enumerate(revs, r -> new Page(r.getProject(), r.getPageId()));
        }

        private Long getUsers(Set<Revision> revs) {
            return enumerate(revs, Revision::getUser);
        }

        private Long getEvents(Set<Revision> revs) {
            Set<Event> events = new HashSet<>();
            revs.forEach(r -> events.addAll(r.getEvents()));
            return Long.valueOf(events.size());
        }
        public Impact getEventImpact(Set<Revision> revs) {

            return new Impact(getCreatedPages(revs),
                              getEditedPages(revs),
                              Long.valueOf(revs.size()),
                              getDiff(revs),
                              getUsers(revs),
                              null);
        }

        public Impact getUserImpact(Set<Revision> revs) {

            return new Impact(getCreatedPages(revs),
                              getEditedPages(revs),
                              Long.valueOf(revs.size()),
                              getDiff(revs),
                              null,
                              getEvents(revs));
        }

    public Impact getUserTagImpact(Set<Revision> revs) {

        return new Impact(getCreatedPages(revs),
                getEditedPages(revs),
                Long.valueOf(revs.size()),
                getDiff(revs),
                getUsers(revs),
                getEvents(revs));
    }

        public Impact getImpact(Event event) {
            return getEventImpact(event.getRevisions());
        }

        private void addRevs(EventTag tag, Set<Revision> revs) {
            revs.addAll(tag.getTagged().stream().flatMap(e -> e.getRevisions().stream()).toList());
            tag.getChildren().forEach(t -> addRevs(t, revs));
        }

        private void addRevs(UserTag tag, Set<Revision> revs) {
            revs.addAll(tag.getTagged().stream().flatMap(e -> e.getRevisions().stream()).toList());
            tag.getChildren().forEach(t -> addRevs(t, revs));
        }

        public Impact getImpact(EventTag tag) {
            Set<Revision> revs = new HashSet<>();
            addRevs(tag, revs);
            return getEventImpact(revs);
        }

    public Impact getImpact(UserTag tag) {
        Set<Revision> revs = new HashSet<>();
        addRevs(tag, revs);
        return getUserTagImpact(revs);
    }

        public Impact getImpact(User user) {
            return getUserImpact(user.getRevisions().stream().filter(r -> r.getEvents().size() > 0).collect(Collectors.toSet()));
        }
}
