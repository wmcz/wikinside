package cz.cvut.fit.wikimetric.dto;

import cz.cvut.fit.wikimetric.entity.EventType;
import cz.cvut.fit.wikimetric.entity.User;

import java.time.Instant;
import java.util.Collection;

public class EventDto {
    private EventType eventType;
    private String name;
    private Instant startDate;
    private Instant endDate;
    private Collection<User> participants;
    private Impact impact;

}
