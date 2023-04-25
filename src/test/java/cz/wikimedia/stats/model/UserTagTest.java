package cz.wikimedia.stats.model;

import cz.wikimedia.stats.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Repeat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class UserTagTest extends BaseTest {

    @Test
    @Repeat(TEST_REPEATS)
    void getId() {
        Long id = randomLong();

        UserTag tag = new UserTag(id, null, null, null, null);

        Assertions.assertEquals(id, tag.getId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getName() {
        String name = randomString();

        UserTag tag = new UserTag(null, name, null, null, null);

        Assertions.assertEquals(name, tag.getName());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setName() {
        String name = randomString();

        UserTag tag = new UserTag(null, randomString(), null, null, null);
        tag = tag.setName(name);

        Assertions.assertEquals(name, tag.getName());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getTagged() {
        Set<User> tagged = new HashSet<>();
        while (tagged.size() < 10) {
            tagged.add(new User(randomLong(), randomLong(), randomString(), randomInstant(), Collections.emptySet(), Collections.emptySet()));
        }

        UserTag tag = new UserTag(null, null, tagged, null, null);

        Assertions.assertEquals(tagged, tag.getTagged());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getParent() {
        UserTag parent = new UserTag(randomLong(), randomString(), null, null, null);

        UserTag child = new UserTag(null, null, null, parent, null);

        Assertions.assertEquals(parent, child.getParent());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getChildren() {
        Set<UserTag> children = new HashSet<>();

        while (children.size() < 10) {
            children.add(new UserTag(randomLong(), randomString(),  null, null, null));
        }

        UserTag parent = new UserTag(null, null, null, null, children);

        Assertions.assertEquals(children, parent.getChildren());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void addTagged() {
        UserTag tag = new UserTag(null, null, new HashSet<>(), null, null);

        Set<User> tagged = new HashSet<>();
        while (tagged.size() < 10) {
            tagged.add(new User(randomLong(), randomLong(), randomString(), randomInstant(), null, null));
        }

        tagged.forEach(tag::addTagged);

        Assertions.assertEquals(tagged, tag.getTagged());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void removeTagged() {
        Set<User> tagged = new HashSet<>();
        while (tagged.size() < 10) {
            tagged.add(new User(randomLong(), randomLong(), randomString(), randomInstant(), null, null));
        }

        UserTag tag = new UserTag(null, null, new HashSet<>(), null, null);

        tagged.forEach(tag::removeTagged);

        Assertions.assertEquals(0, tag.getTagged().size());
    }
    @Test
    @Repeat(TEST_REPEATS)
    void addChild() {
        Set<UserTag> children = new HashSet<>();
        while (children.size() < 10) {
            children.add(new UserTag(randomLong(), randomString(), null, null, null));
        }

        UserTag tag = new UserTag(null, null, null, null, new HashSet<>());

        children.forEach(tag::addChild);

        Assertions.assertEquals(children, tag.getChildren());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void removeChild() {
        Set<UserTag> children = new HashSet<>();
        while (children.size() < 10) {
            children.add(new UserTag(randomLong(), randomString(), null, null, null));
        }

        UserTag tag = new UserTag(null, null, null, null, new HashSet<>(children));

        children.forEach(tag::removeChild);

        Assertions.assertEquals(0, tag.getChildren().size());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void setParent() {
        UserTag parent1 = new UserTag(randomLong(), randomString(), null, null, new HashSet<>());
        UserTag parent2 = new UserTag(randomLong(), randomString(), null, null, new HashSet<>());

        UserTag child = new UserTag(null, null, null, parent1, null);

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

        UserTag tag1 = new UserTag(id1, null, null, null, null);
        UserTag tag2 = new UserTag(id1, null, null, null, null);

        UserTag tag3 = new UserTag(id2, null, null, null, null);
        UserTag tag4 = new UserTag(id2, null, null, null, null);

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