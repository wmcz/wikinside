package cz.wikimedia.stats.model;

import cz.wikimedia.stats.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Repeat;

import java.util.HashSet;
import java.util.Set;

class EventTagTest extends BaseTest {

    @Test
    @Repeat(TEST_REPEATS)
    void getId() {
        Long id = randomLong();

        EventTag tag = new EventTag(id, null, null, null, null);

        Assertions.assertEquals(id, tag.getId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getName() {
        String name = randomString();

        EventTag tag = new EventTag(null, name, null, null, null);

        Assertions.assertEquals(name, tag.getName());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setName() {
        String name = randomString();

        EventTag tag = new EventTag(null, randomString(), null, null, null);
        tag = tag.setName(name);

        Assertions.assertEquals(name, tag.getName());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getTagged() {
        Set<Event> tagged = new HashSet<>();
        while (tagged.size() < 10) {
            tagged.add(new Event(randomLong(), null, randomString(), randomString(), randomDate(), randomDate(), null, null, null));
        }

        EventTag tag = new EventTag(null, null, tagged, null, null);

        Assertions.assertEquals(tagged, tag.getTagged());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getParent() {
        EventTag parent = new EventTag(randomLong(), randomString(), null, null, null);

        EventTag child = new EventTag(null, null, null, parent, null);

        Assertions.assertEquals(parent, child.getParent());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getChildren() {
        Set<EventTag> children = new HashSet<>();

        while (children.size() < 10) {
            children.add(new EventTag(randomLong(), randomString(),  null, null, null));
        }

        EventTag parent = new EventTag(null, null, null, null, children);

        Assertions.assertEquals(children, parent.getChildren());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void addTagged() {
        EventTag tag = new EventTag(null, null, new HashSet<>(), null, null);

        Set<Event> tagged = new HashSet<>();
        while (tagged.size() < 10) {
            tagged.add(new Event(randomLong(), null, randomString(), randomString(), randomDate(), randomDate(), null, null, null));
        }

        tagged.forEach(tag::addTagged);

        Assertions.assertEquals(tagged, tag.getTagged());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void removeTagged() {
        Set<Event> tagged = new HashSet<>();
        while (tagged.size() < 10) {
            tagged.add(new Event(randomLong(), null, randomString(), randomString(), randomDate(), randomDate(), null, null, null));
        }

        EventTag tag = new EventTag(null, null, new HashSet<>(), null, null);

        tagged.forEach(tag::removeTagged);

        Assertions.assertEquals(0, tag.getTagged().size());
    }
    @Test
    @Repeat(TEST_REPEATS)
    void addChild() {
        Set<EventTag> children = new HashSet<>();
        while (children.size() < 10) {
            children.add(new EventTag(randomLong(), randomString(), null, null, null));
        }

        EventTag tag = new EventTag(null, null, null, null, new HashSet<>());

        children.forEach(tag::addChild);

        Assertions.assertEquals(children, tag.getChildren());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void removeChild() {
        Set<EventTag> children = new HashSet<>();
        while (children.size() < 10) {
            children.add(new EventTag(randomLong(), randomString(), null, null, null));
        }

        EventTag tag = new EventTag(null, null, null, null, new HashSet<>(children));

        children.forEach(tag::removeChild);

        Assertions.assertEquals(0, tag.getChildren().size());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setParent() {
        EventTag parent1 = new EventTag(randomLong(), randomString(), null, null, new HashSet<>());
        EventTag parent2 = new EventTag(randomLong(), randomString(), null, null, new HashSet<>());

        EventTag child = new EventTag(null, null, null, parent1, null);

        child.setParent(parent2);

        Assertions.assertEquals(parent2, child.getParent());

        child.setParent(null);

        Assertions.assertNull(child.getParent());

    }

    @Test
    void testEquals() {
        Long id1 = randomLong();
        Long id2 = randomLong();

        while (id1.equals(id2)) {
            id2 = randomLong();
        }

        EventTag tag1 = new EventTag(id1, null, null, null, null);
        EventTag tag2 = new EventTag(id1, null, null, null, null);

        EventTag tag3 = new EventTag(id2, null, null, null, null);
        EventTag tag4 = new EventTag(id2, null, null, null, null);

        Assertions.assertTrue(tag1.equals(tag2));
        Assertions.assertTrue(tag2.equals(tag1));

        Assertions.assertTrue(tag3.equals(tag4));
        Assertions.assertTrue(tag4.equals(tag3));

        Assertions.assertFalse(tag1.equals(tag3));
        Assertions.assertFalse(tag3.equals(tag1));

        Assertions.assertFalse(tag1.equals(tag4));
        Assertions.assertFalse(tag4.equals(tag1));

        Assertions.assertFalse(tag2.equals(tag4));
        Assertions.assertFalse(tag4.equals(tag2));
    }
}