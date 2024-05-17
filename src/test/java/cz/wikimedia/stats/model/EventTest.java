package cz.wikimedia.stats.model;

import cz.wikimedia.stats.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Repeat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

class EventTest extends BaseTest {

    @Test
    @Repeat(TEST_REPEATS)
    void getId() {
        Long id = randomLong();

        Event event = new Event(id, null, null, null, null, null, null, null, null, null, null, null);

        Assertions.assertEquals(id, event.getId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getName() {
        String name = randomString();

        Event event = new Event(null, null, null, name, null, null, null, null, null, null, null, null);

        Assertions.assertEquals(name, event.getName());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getStartDate() {
        LocalDate date = randomDate();

        Event event = new Event(null, null, null, null, null, null, date, null, null, null, null, null);

        Assertions.assertEquals(date, event.getStartDate());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getEndDate() {
        LocalDate date = randomDate();

        Event event = new Event(null, null, null, null, null, null, null, date, null, null, null, null);

        Assertions.assertEquals(date, event.getEndDate());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getHashtag() {
        String tag = randomString();

        Event event = new Event(null, null, null, null, null, tag, null, null, null, null, null, null);

        Assertions.assertEquals(tag, event.getCategory());
    }

    @Test
    void testEquals() {
        Long id1 = randomLong();
        Long id2 = randomLong();

        while (id1.equals(id2)) {
            id2 = randomLong();
        }

        Event event1 = new Event(id1, null, null, null, null, null, null, null, null, null, null, null);
        Event event2 = new Event(id1, null, null, null, null, null, null, null, null, null, null, null);

        Event event3 = new Event(id2, null, null, null, null, null, null, null, null, null, null, null);
        Event event4 = new Event(id2, null, null, null, null, null, null, null, null, null, null, null);

        Assertions.assertEquals(event1, event2);
        Assertions.assertEquals(event2, event1);

        Assertions.assertEquals(event3, event4);
        Assertions.assertEquals(event4, event3);

        Assertions.assertNotEquals(event1, event3);
        Assertions.assertNotEquals(event3, event1);

        Assertions.assertNotEquals(event1, event4);
        Assertions.assertNotEquals(event4, event1);

        Assertions.assertNotEquals(event2, event4);
        Assertions.assertNotEquals(event4, event2);

    }

    @Test
    void getTags() {
        Set<EventTag> tags = new HashSet<>();
        while (tags.size() < 10) {
            tags.add(new EventTag(randomLong(), randomString(), new HashSet<>(), null, null));
        }

        Event event = new Event(null, tags, null, null, null, null, null, null, null, null, null, null);

        Assertions.assertEquals(tags, event.getTags());
    }

    @Test
    void getParticipants() {
        Set<User> users = new HashSet<>();
        while (users.size() < 10) {
            users.add(new User(randomLong(), randomString()));
        }

        Event event = new Event(null, null, null, null, null, null, null, null, users, null, null, null);

        Assertions.assertEquals(users, event.getParticipants());
    }

    @Test
    void getProjects() {
        Set<Project> projects = new HashSet<>();
        while (projects.size() < 10) {
            projects.add(new Project(randomLong(), randomString(), randomString()));
        }

        Event event = new Event(null, null, null, null, null, null, null, null, null, projects, null, null);

        Assertions.assertEquals(projects, event.getProjects());
    }

    @Test
    void getRevisions() {
        Project project = new Project(randomLong(), randomString(), randomString());
        Set<Revision> revisions = new HashSet<>();
        while (revisions.size() < 10) {
            revisions.add(new Revision(randomLong(), randomLong(), randomLong(), randomLong(), null, null, null, project, null, null));
        }

        Event event = new Event(null, null, null, null, null, null, null, null, null, null, revisions, null);

        Assertions.assertEquals(revisions, event.getRevisions());
    }

    @Test
    void addTag() {
        Set<EventTag> tags = new HashSet<>();
        while (tags.size() < 10) {
            tags.add(new EventTag(randomLong(), randomString(), new HashSet<>(), null, null));
        }

        Event event = new Event(null, new HashSet<>(), null, null, null, null, null, null, null, null, null, null);

        tags.forEach(event::addTag);

        Assertions.assertEquals(tags, event.getTags());
    }

    @Test
    void removeTag() {
        Set<EventTag> tags = new HashSet<>();
        while (tags.size() < 10) {
            tags.add(new EventTag(randomLong(), randomString(), new HashSet<>(), null, null));
        }

        Event event = new Event(null, new HashSet<>(tags), null, null, null, null, null, null, null, null, null, null);

        tags.forEach(event::removeTag);

        Assertions.assertEquals(0, event.getTags().size());
    }

    @Test
    void addParticipant() {
        Set<User> users = new HashSet<>();
        while (users.size() < 10) {
            users.add(new User(randomLong(), randomString()));
        }

        Event event = new Event(null, null, null, null, Event.DataCollectionStrategy.MANUAL, null, null, null, new HashSet<>(), null, null, null);

        users.forEach(event::addParticipant);

        Assertions.assertEquals(users, event.getParticipants());
    }

    @Test
    void removeParticipant() {
        Set<User> users = new HashSet<>();
        while (users.size() < 10) {
            users.add(new User(randomLong(), randomString()));
        }

        Event event = new Event(null, null, null, null, Event.DataCollectionStrategy.MANUAL, null, null, null, new HashSet<>(users), null, null, null);

        users.forEach(event::removeParticipant);

        Assertions.assertEquals(0, event.getParticipants().size());
    }

    @Test
    void addRevision() {
        Project project = new Project(randomLong(), randomString(), randomString());
        Set<Revision> revisions = new HashSet<>();
        while (revisions.size() < 10) {
            revisions.add(new Revision(randomLong(), randomLong(), randomLong(), randomLong(), null, null, null, project, null, null));
        }

        Event event = new Event(null, null, null, null, null, null, null, null, new HashSet<>(), null, new HashSet<>(), null);

        revisions.forEach(event::addRevision);

        Assertions.assertEquals(revisions, event.getRevisions());
    }

    @Test
    void removeRevision() {
        Project project = new Project(randomLong(), randomString(), randomString());
        Set<Revision> revisions = new HashSet<>();
        while (revisions.size() < 10) {
            revisions.add(new Revision(randomLong(), randomLong(), randomLong(), randomLong(), null, null, null, project, null, null));
        }

        Event event = new Event(null, null, null, null, null, null, null, null, new HashSet<>(), null, new HashSet<>(revisions), new HashSet<>());

        revisions.forEach(event::removeRevision);

        Assertions.assertEquals(0, event.getRevisions().size());
    }
}