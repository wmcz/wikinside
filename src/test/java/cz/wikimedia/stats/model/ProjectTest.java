package cz.wikimedia.stats.model;

import cz.wikimedia.stats.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Repeat;

class ProjectTest extends BaseTest {

    @Test
    @Repeat(TEST_REPEATS)
    void getId() {
        Long id = randomLong();

        Project project = new Project(id, null, null);

        Assertions.assertEquals(id, project.getId());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getName() {
    String name = randomString();

    Project project = new Project(null, name, null);

    Assertions.assertEquals(name, project.getName());
    }

    @Test
    @Repeat(TEST_REPEATS)
    void getPath() {
        String path = randomString();

        Project project = new Project(null, null, path);

        Assertions.assertEquals(path, project.getPath());
    }

    @Test
    void testEquals() {
        String path1 = randomString();
        String path2 = randomString();

        while (path1.equals(path2)) {
            path2 = randomString();
        }

        Project project1 = new Project(null, null, path1);
        Project project2 = new Project(null, null, path1);

        Project project3 = new Project(null, null, path2);
        Project project4 = new Project(null, null, path2);

        Assertions.assertTrue(project1.equals(project2));
        Assertions.assertTrue(project2.equals(project1));

        Assertions.assertTrue(project3.equals(project4));
        Assertions.assertTrue(project4.equals(project3));

        Assertions.assertFalse(project1.equals(project3));
        Assertions.assertFalse(project3.equals(project1));

        Assertions.assertFalse(project1.equals(project4));
        Assertions.assertFalse(project4.equals(project1));

        Assertions.assertFalse(project2.equals(project4));
        Assertions.assertFalse(project4.equals(project2));
    }
}