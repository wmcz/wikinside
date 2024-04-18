package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.model.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

        private <E, T> Long enumerate(Set<T> revs, Function<T, E> forEach) {
            Set<E> set = new HashSet<>();
            revs.forEach(r -> set.add(forEach.apply(r)));
            return Long.valueOf(set.size());
        }

        private Long getEditedPages(Set<Revision> revs) {
            return enumerate(revs, r -> new Page(r.getProject(), r.getPageId()));
        }

        private Long getUsers(Set<Revision> revs, Set<Image> images) {
            return enumerate(revs, Revision::getUser) + enumerate(images, Image::getUser);
        }

        private Long getEvents(Set<Revision> revs) {
            Set<Event> events = new HashSet<>();
            revs.forEach(r -> events.addAll(r.getEvents()));
            return Long.valueOf(events.size());
        }

        private Long collectUsage(Set<Image> images) {
            if (images.isEmpty()) return null;
            else return enumerate(images, Image::getUsage);
        }

        public Impact getEventImpact(Set<Revision> revs, Set<Image> images) {

            return new Impact(getCreatedPages(revs),
                              getEditedPages(revs),
                              Long.valueOf(revs.size()),
                              getDiff(revs),
                              getUsers(revs, images),
                              null,
                              images.isEmpty() ? null : Long.valueOf(images.size()),
                              collectUsage(images));
        }

        public Impact getUserImpact(Set<Revision> revs, Set<Image> images) {

            return new Impact(getCreatedPages(revs),
                              getEditedPages(revs),
                              Long.valueOf(revs.size()),
                              getDiff(revs),
                              null,
                              getEvents(revs),
                              images.isEmpty() ? null : Long.valueOf(images.size()),
                              collectUsage(images));
        }

        public Impact getImpact(Event event) {
            return getEventImpact(event.getRevisions(), event.getImages());
        }

        private <T, E> void populate(Tag<E> tag, Set<T> elems, Function<E, Collection<T>> getter) {
            elems.addAll(tag.getTagged().stream().flatMap(e -> getter.apply(e).stream()).toList());
            tag.getChildren().forEach(t -> populate(t, elems, getter));
        }

        public Impact getImpact(EventTag tag) {
            Set<Revision> revs = new HashSet<>();
            Set<Image> images = new HashSet<>();
            populate(tag, revs, Event::getRevisions);
            populate(tag, images, Event::getImages);
            return getEventImpact(revs, images);
        }

        public Impact getImpact(UserTag tag) {
            Set<Revision> revs = new HashSet<>();
            Set<Image> images = new HashSet<>();
            populate(tag, revs, User::getRevisions);
            populate(tag, images, User::getImages);
            return getUserImpact(revs, images);
        }

        public Impact getImpact(User user) {
            return getUserImpact(user.getRevisions(), user.getImages());
        }
}
