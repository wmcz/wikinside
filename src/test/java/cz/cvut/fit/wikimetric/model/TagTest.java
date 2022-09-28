package cz.cvut.fit.wikimetric.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagTest {

    @Test
    void getName() {
        String tagName = "tag";

        Tag tag = new Tag(tagName);
        assert tag.getName().equals(tagName);
    }

    @Test
    void setName() {
        String tagName = "tag";
        String newName = "new";

        Tag tag = new Tag(tagName);
        assert tag.getName().equals(tagName);

        tag.setName(newName);
        assert tag.getName().equals(newName);
    }
}