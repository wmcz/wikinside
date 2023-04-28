package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.BaseTest;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.EventTag;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class ImpactServiceTest extends BaseTest {

    ImpactService service = new ImpactService();

    @Test
    void getImpact() {
        Project project = new Project(1000L, "name", "path");
        Set<Revision> revs = new HashSet<>();

        while (revs.size() < 100) {
            revs.add(new Revision(randomLong(), randomLong(), randomLong(), randomLong(), randomBool() ? 0 : randomLong(), null, null, project, randomInstant(), randomString()));
        }

        Event event = new Event(1L, null, null, null, null, null, null, null, revs);
        EventTag tag = new EventTag(2L, null, Set.of(event), null, Collections.emptySet());

        Assertions.assertEquals(service.getImpact(event.getRevisions()), service.getImpact(event));
        Assertions.assertEquals(service.getImpact(event.getRevisions()), service.getImpact(tag));

        //20 revs overlap
        Set<Revision> firstRevs = revs.stream().limit(60).collect(Collectors.toSet());
        Set<Revision> secondRevs = revs.stream().skip(40).collect(Collectors.toSet());

        Event event1 = new Event(3L, null, null, null, null, null, null, null, firstRevs);
        Event event2 = new Event(4L, null, null, null, null, null, null, null, secondRevs);

        EventTag tagWithBoth = new EventTag(2L, null, Set.of(event1, event2), null, Collections.emptySet());

        Assertions.assertEquals(service.getImpact(revs), service.getImpact(tagWithBoth));

    }

}