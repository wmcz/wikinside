package cz.cvut.fit.wikimetric.api.dto;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.wikimetric.api.Views;

import java.util.Collection;


public record UserDto(@JsonView(Views.Incoming.class) Long id,
                      @JsonView(Views.Incoming.class) String username,
                      @JsonView(Views.Incoming.class) String email,
                      @JsonView(Views.Incoming.class) Collection<Long> tagIds,
                      @JsonView(Views.Outgoing.class)Collection<Long> eventIds) {}


