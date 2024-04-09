package cz.wikimedia.stats.model;

import cz.wikimedia.stats.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Repeat;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

class RevisionTest extends BaseTest {

    @Test
    @Repeat(TEST_REPEATS)
    void getId() {
        Long id = randomLong();

        Revision rev = new Revision(id, null, null, null, null, null, null, null, null, null);

        Assertions.assertEquals(id, rev.getId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getRevId() {
        Long id = randomLong();

        Revision rev = new Revision(null, id, null, null, null, null, null, null, null, null);

        Assertions.assertEquals(id, rev.getRevId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getDiff() {
        Long diff = randomLong();

        Revision rev = new Revision(null, null, diff, null, null, null, null, null, null, null);

        Assertions.assertEquals(diff, rev.getDiff());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setDiff() {
        Long diff = randomLong();

        Revision rev = new Revision(null, null, randomLong(), null, null, null, null, null, null, null);
        rev.setDiff(diff);

        Assertions.assertEquals(diff, rev.getDiff());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getPageId() {
        Long id = randomLong();

        Revision rev = new Revision(null, null, null, id, null, null, null, null, null, null);

        Assertions.assertEquals(id, rev.getPageId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getParentId() {
        Long id = randomLong();

        Revision rev = new Revision(null, null, null, null, id, null, null, null, null, null);

        Assertions.assertEquals(id, rev.getParentId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void isPageCreation() {
        Long id = randomBool() ? 0L : randomLong();

        Revision rev = new Revision(null, null, null, null, id, null, null, null, null, null);

        Assertions.assertEquals(id == 0, rev.isPageCreation());
    }

    @Test
    void getProject() {
        Project project = new Project(randomLong(), randomString(), randomString());
        Revision rev = new Revision(null, null, null, null, null, null, null, project, null, null);

        Assertions.assertEquals(project, rev.getProject());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getTimestamp() {
        Instant timestamp = randomInstant();

        Revision rev = new Revision(null, null, null, null, null, null, null, null, timestamp, null);

        Assertions.assertEquals(timestamp, rev.getTimestamp());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getSummary() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < randomLong(500L - TEST_STR_LENGTH); i += TEST_STR_LENGTH) {
            //summary can be up to 500 characters long
            builder.append(randomString());
        }
        String summary = builder.toString();

        Revision rev = new Revision(null, null, null, null, null, null, null, null, null, summary);

        Assertions.assertEquals(summary, rev.getSummary());
    }

    @Test
    void testEquals() {
        Project project1 = new Project(0L, "string1", "path1");
        Project project2 = new Project(1L, "string2", "path2");

        Long revId1 = randomLong();
        Long revId2 = randomLong();

        while (revId1.equals(revId2)) {
            revId2 = randomLong();
        }

        Revision rev1 = new Revision(null, revId1, null, null, null, null, null, project1, null, null);
        Revision rev2 = new Revision(null, revId1, null, null, null, null, null, project1, null, null);

        Revision rev3 = new Revision(null, revId2, null, null, null, null, null, project1, null, null);
        Revision rev4 = new Revision(null, revId2, null, null, null, null, null, project1, null, null);

        Revision rev5 = new Revision(null, revId1, null, null, null, null, null, project2, null, null);
        Revision rev6 = new Revision(null, revId1, null, null, null, null, null, project2, null, null);

        Assertions.assertTrue(rev1.equals(rev2));
        Assertions.assertTrue(rev2.equals(rev1));

        Assertions.assertTrue(rev3.equals(rev4));
        Assertions.assertTrue(rev4.equals(rev3));

        Assertions.assertTrue(rev5.equals(rev6));
        Assertions.assertTrue(rev6.equals(rev5));

        Assertions.assertFalse(rev1.equals(rev3));
        Assertions.assertFalse(rev3.equals(rev1));

        Assertions.assertFalse(rev1.equals(rev4));
        Assertions.assertFalse(rev4.equals(rev1));

        Assertions.assertFalse(rev1.equals(rev5));
        Assertions.assertFalse(rev5.equals(rev1));

        Assertions.assertFalse(rev2.equals(rev6));
        Assertions.assertFalse(rev6.equals(rev2));

        Assertions.assertFalse(rev3.equals(rev5));
        Assertions.assertFalse(rev5.equals(rev3));
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getUser() {
        User user = new User(randomLong(), randomString());

        Revision rev = new Revision(null, null, null, null, null, user, null, null, null, null);

        Assertions.assertEquals(user, rev.getUser());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getEvents() {
        Set<Event> events = new HashSet<>();

        while (events.size() < 10) {
            events.add(new Event(randomLong(), null, randomString(), null, null, randomDate(), randomDate(), null, null, null, null));
        }

        Revision rev = new Revision(null, null, null, null, null, null, events, null, null, null);

        Assertions.assertEquals(events, rev.getEvents());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void addEvent() {
        Set<Event> events = new HashSet<>();

        while (events.size() < 10) {
            events.add(new Event(randomLong(), null, randomString(), null, null, randomDate(), randomDate(), null, null, null, null));
        }

        Revision rev = new Revision(null, null, null, null, null, null, new HashSet<>(), null, null, null);

        events.forEach(rev::addEvent);

        Assertions.assertEquals(events, rev.getEvents());
    }
}