package cz.cvut.fit.wikimetric.entity;

import org.junit.jupiter.api.Test;

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
        Date startDate = new Date(1000213391000L);

        Event event = new Event(new EventType(), "name", startDate, new Date(startDate.getTime() + 2048000), new HashSet<User>());
        assert event.getStartDate() == startDate;
    }

    @Test
    void getEndDate() {
        Date endDate = new Date(1000215439000L);

        Event event = new Event(new EventType(), "name", new Date(endDate.getTime() - 2048000), endDate, new HashSet<User>());
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
        Date startDate = new Date(1000213391000L);
        Date newDate = new Date(0);

        Event event = new Event(new EventType(), "name", startDate, new Date(startDate.getTime() + 2048000), new HashSet<User>());
        assert event.getStartDate() == startDate;

        event.setStartDate(newDate);
        assert event.getStartDate() == newDate;
    }

    @Test
    void setEndDate() {

        Date endDate = new Date(2048000);
        Date newDate = new Date(1000215439000L);

        Event event = new Event(new EventType(), "name", new Date(endDate.getTime() - 2048000), endDate, new HashSet<User>());
        assert event.getEndDate() == endDate;

        event.setEndDate(newDate);
        assert event.getEndDate() == newDate;
    }
}