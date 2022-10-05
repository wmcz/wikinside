package cz.cvut.fit.wikimetric.dto;

import cz.cvut.fit.wikimetric.entity.Event;
import cz.cvut.fit.wikimetric.entity.Tag;

import java.util.Collection;

public class UserDto {
    private Long id;
    private String email;
    private Collection<Tag> tags;
    private Collection<Event> participatedIn;
    private Impact impact;
}
