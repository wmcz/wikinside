package cz.cvut.fit.wikimetric.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;

class EventTest {

    @Test
    void getName() {
        String eventName = "name";

        Event event = new Event(eventName);
        assert event.getName().equals(eventName);
    }

    @Test
    void getStartDate() {
        Instant startDate = Instant.ofEpochSecond(1000213391);

        Event event = new Event(new EventType(), "name", startDate, startDate.plusSeconds(2048), new HashSet<User>());
        assert event.getStartDate() == startDate;
    }

    @Test
    void getEndDate() {
        Instant endDate = Instant.ofEpochSecond(1000215439);

        Event event = new Event(new EventType(), "name", endDate.minusSeconds(2048), endDate, new HashSet<User>());
        assert event.getEndDate() == endDate;
    }


    @Test
    void setName() {
        String eventName = "name";
        String newName = "new";

        Event event = new Event(eventName);
        assert event.getName().equals(eventName);

        event.setName(newName);
        assert event.getName().equals(newName);
    }

    @Test
    void setStartDate() {
        Instant startDate = Instant.ofEpochSecond(1000213391);
        Instant newDate = Instant.EPOCH;

        Event event = new Event(new EventType(), "name", startDate, startDate.plusSeconds(2048), new HashSet<User>());
        assert event.getStartDate() == startDate;

        event.setStartDate(newDate);
        assert event.getStartDate() == newDate;
    }

    @Test
    void setEndDate() {

        Instant endDate = Instant.ofEpochSecond(2048);
        Instant newDate = Instant.ofEpochSecond(1000215439);

        Event event = new Event(new EventType(), "name", endDate.minusSeconds(2048), endDate, new HashSet<User>());
        assert event.getEndDate() == endDate;

        event.setEndDate(newDate);
        assert event.getEndDate() == newDate;
    }
}