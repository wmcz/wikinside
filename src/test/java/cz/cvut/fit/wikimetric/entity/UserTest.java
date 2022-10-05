package cz.cvut.fit.wikimetric.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTest {
    @Test
    void getId() {
        User user = new User(1L);
        assert user.getId() == 1;
    }
}