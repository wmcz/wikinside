package cz.wikimedia.stats;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;

public class BaseTest {
    public final static int TEST_REPEATS = 100;
    public final static int TEST_STR_LENGTH = 64;

    private final Random rand;

    public BaseTest() {
        this.rand = new Random();
    }

    public String randomString() {
        return rand
                .ints()
                .filter(Character::isDefined)
                .limit(TEST_STR_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

    public Long randomLong() {
        return rand.nextLong();
    }

    public Long randomLong(Long limit) {
        return rand.nextLong(limit);
    }

    public LocalDate randomDate() {
        return LocalDate.EPOCH.plusDays(randomLong(30000L));
    }

    public Instant randomInstant() {
        return Instant.EPOCH.plusSeconds(randomLong(3000000000L));
    }

    public Boolean randomBool() {
        return rand.nextBoolean();
    }
}
