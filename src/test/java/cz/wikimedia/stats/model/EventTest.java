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

        Event event = new Event(id, null, null, null, null, null, null, null, null);

        Assertions.assertEquals(id, event.getId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getName() {
        String name = randomString();

        Event event = new Event(name);

        Assertions.assertEquals(name, event.getName());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getStartDate() {
        LocalDate date = randomDate();

        Event event = new Event(null, null, null, null, date, null, null, null, null);

        Assertions.assertEquals(date, event.getStartDate());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getEndDate() {
        LocalDate date = randomDate();

        Event event = new Event(null, null, null, null, null, date, null, null, null);

        Assertions.assertEquals(date, event.getEndDate());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getHashtag() {
        String tag = randomString();

        Event event = new Event(null, null, null, tag, null, null, null, null, null);

        Assertions.assertEquals(tag, event.getCategory());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setName() {
        String newName = randomString();


        Event event = new Event(randomString());
        event = event.setName(newName);

        Assertions.assertEquals(newName, event.getName());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setStartDate() {
        LocalDate date = randomDate();
        Event event = new Event(null, null, null, null, randomDate(), null, null, null, null);
        event = event.setStartDate(date);
        Assertions.assertEquals(date, event.getStartDate());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setEndDate() {
        LocalDate date = randomDate();
        Event event = new Event(null, null, null, null, null, randomDate(), null, null, null);
        event = event.setStartDate(date);
        Assertions.assertEquals(date, event.getStartDate());
    }
    @Test
    @Repeat(TEST_REPEATS)
    void setHashtag() {
        String tag = randomString();
        Event event = new Event(null, null, null, randomString(), null, null, null, null, null);
        event = event.setCategory(tag);
        Assertions.assertEquals(tag, event.getCategory());
    }

    @Test
    void testEquals() {
        Long id1 = randomLong();
        Long id2 = randomLong();

        while (id1.equals(id2)) {
            id2 = randomLong();
        }

        Event event1 = new Event(id1, null, null, null, null, null, null, null, null);
        Event event2 = new Event(id1, null, null, null, null, null, null, null, null);

        Event event3 = new Event(id2, null, null, null, null, null, null, null, null);
        Event event4 = new Event(id2, null, null, null, null, null, null, null, null);

        Assertions.assertTrue(event1.equals(event2));
        Assertions.assertTrue(event2.equals(event1));

        Assertions.assertTrue(event3.equals(event4));
        Assertions.assertTrue(event4.equals(event3));

        Assertions.assertFalse(event1.equals(event3));
        Assertions.assertFalse(event3.equals(event1));

        Assertions.assertFalse(event1.equals(event4));
        Assertions.assertFalse(event4.equals(event1));

        Assertions.assertFalse(event2.equals(event4));
        Assertions.assertFalse(event4.equals(event2));

    }

    @Test
    void getTags() {
        Set<EventTag> tags = new HashSet<>();
        while (tags.size() < 10) {
            tags.add(new EventTag(randomLong(), randomString(), new HashSet<>(), null, null));
        }

        Event event = new Event(null, tags, null, null, null, null, null, null, null);

        Assertions.assertEquals(tags, event.getTags());
    }

    @Test
    void getParticipants() {
        Set<User> users = new HashSet<>();
        while (users.size() < 10) {
            users.add(new User(randomLong(), randomString()));
        }

        Event event = new Event(null, null, null, null, null, null, users, null, null);

        Assertions.assertEquals(users, event.getParticipants());
    }

    @Test
    void getProjects() {
        Set<Project> projects = new HashSet<>();
        while (projects.size() < 10) {
            projects.add(new Project(randomLong(), randomString(), randomString()));
        }

        Event event = new Event(null, null, null, null, null, null, null, projects, null);

        Assertions.assertEquals(projects, event.getProjects());
    }

    @Test
    void getRevisions() {
        Project project = new Project(randomLong(), randomString(), randomString());
        Set<Revision> revisions = new HashSet<>();
        while (revisions.size() < 10) {
            revisions.add(new Revision(randomLong(), randomLong(), randomLong(), randomLong(), null, null, null, project, null, null));
        }

        Event event = new Event(null, null, null, null, null, null, null, null, revisions);

        Assertions.assertEquals(revisions, event.getRevisions());
    }

    @Test
    void addTag() {
        Set<EventTag> tags = new HashSet<>();
        while (tags.size() < 10) {
            tags.add(new EventTag(randomLong(), randomString(), new HashSet<>(), null, null));
        }

        Event event = new Event(null, new HashSet<>(), null, null, null, null, null, null, null);

        tags.forEach(event::addTag);

        Assertions.assertEquals(tags, event.getTags());
    }

    @Test
    void removeTag() {
        Set<EventTag> tags = new HashSet<>();
        while (tags.size() < 10) {
            tags.add(new EventTag(randomLong(), randomString(), new HashSet<>(), null, null));
        }

        Event event = new Event(null, new HashSet<>(tags), null, null, null, null, null, null, null);

        tags.forEach(event::removeTag);

        Assertions.assertEquals(0, event.getTags().size());
    }

    @Test
    void addParticipant() {
        Set<User> users = new HashSet<>();
        while (users.size() < 10) {
            users.add(new User(randomLong(), randomString()));
        }

        Event event = new Event(null, null, null, null, null, null, new HashSet<>(), null, null);

        users.forEach(event::addParticipant);

        Assertions.assertEquals(users, event.getParticipants());
    }

    @Test
    void removeParticipant() {
        Set<User> users = new HashSet<>();
        while (users.size() < 10) {
            users.add(new User(randomLong(), randomString()));
        }

        Event event = new Event(null, null, null, null, null, null, new HashSet<>(users), null, null);

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

        Event event = new Event(null, null, null, null, null, null, null, null, new HashSet<>());

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

        Event event = new Event(null, null, null, null, null, null, null, null, new HashSet<>(revisions));

        revisions.forEach(event::removeRevision);

        Assertions.assertEquals(0, event.getRevisions().size());
    }
}