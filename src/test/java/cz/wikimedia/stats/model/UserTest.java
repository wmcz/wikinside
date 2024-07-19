package cz.wikimedia.stats.model;

import cz.wikimedia.stats.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Repeat;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


class UserTest extends BaseTest {

    @Test
    @Repeat(TEST_REPEATS)
    void getId() {
        Long id = randomLong();

        User user = new User(id, null, null, null, null, null);

        Assertions.assertEquals(id, user.getId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getUsername() {
        String name = randomString();

        User user = new User(null, null, name, null, null, null);

        Assertions.assertEquals(name, user.getUsername());

    }

    @Test
    @Repeat(TEST_REPEATS)
    void getRegistration() {
        Instant registration = randomInstant();

        User user = new User(null, null, null, registration, null, null);

        Assertions.assertEquals(registration, user.getRegistration());

    }

    @Test
    @Repeat(TEST_REPEATS)
    void getLocalId() {
        Long id = randomLong();

        User user = new User(null, id, null, null, null, null);

        Assertions.assertEquals(id, user.getLocalId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setUsername() {
        String name = randomString();

        User user = new User(null, null, randomString(), null, null, null);
        user = user.setUsername(name);

        Assertions.assertEquals(name, user.getUsername());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setId() {
        Long id = randomLong();

        User user = new User(randomLong(), null, null, null, null, null);
        user = user.setId(id);

        Assertions.assertEquals(id, user.getId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setLocalId() {
        Long id = randomLong();

        User user = new User(null, randomLong(), null, null, null, null);
        user = user.setLocalId(id);

        Assertions.assertEquals(id, user.getLocalId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setRegistration() {
        Instant registration = randomInstant();

        User user = new User(null, null, null, randomInstant(), null, null);
        user = user.setRegistration(registration);

        Assertions.assertEquals(registration, user.getRegistration());
    }

    @Test
    void testEquals() {
        Long id1 = randomLong();
        Long id2 = randomLong();

        while (id1.equals(id2)) {
            id2 = randomLong();
        }

        User user1 = new User(id1, null, null, null, null, null);
        User user2 = new User(id1, null, null, null, null, null);

        User user3 = new User(id2, null, null, null, null, null);
        User user4 = new User(id2, null, null, null, null, null);

        Assertions.assertEquals(user1, user2);
        Assertions.assertEquals(user2, user1);

        Assertions.assertEquals(user3, user4);
        Assertions.assertEquals(user4, user3);

        Assertions.assertNotEquals(user1, user3);
        Assertions.assertNotEquals(user3, user1);

        Assertions.assertNotEquals(user1, user4);
        Assertions.assertNotEquals(user4, user1);

        Assertions.assertNotEquals(user2, user4);
        Assertions.assertNotEquals(user4, user2);
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getTags() {
        Set<UserTag> tags = new HashSet<>();

        while (tags.size() < 10) {
            tags.add(new UserTag(randomLong(), randomString(), null, null, null, null));
        }

        User user = new User(null, null, null, null, tags, new HashSet<>());

        Assertions.assertEquals(tags, user.getTags());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getEvents() {
        Set<Event> events = new HashSet<>();

        while (events.size() < 10) {
            events.add(new Event(randomLong(), null, null, randomString(), null, randomString(), randomDate(), randomDate(), null, null, null, null));
        }

        User user = new User(null, null, null, null, null,events);

        Assertions.assertEquals(events, user.getEvents());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void addTag() {
        Set<UserTag> tags = new HashSet<>();

        while (tags.size() < 10) {
            tags.add(new UserTag(randomLong(), randomString(), new HashSet<>(), new HashSet<>(), null, null));
        }

        User user = new User(null, null, null, null, new HashSet<>(), new HashSet<>());

        tags.forEach(user::addTag);

        Assertions.assertEquals(tags, user.getTags());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void removeTag() {
        Set<UserTag> tags = new HashSet<>();

        while (tags.size() < 10) {
            tags.add(new UserTag(randomLong(), randomString(), new HashSet<>(), new HashSet<>(), null, null));
        }

        User user = new User(null, null, null, null, new HashSet<>(tags), new HashSet<>());

        tags.forEach(user::removeTag);

        Assertions.assertEquals(0, user.getTags().size());
    }
}