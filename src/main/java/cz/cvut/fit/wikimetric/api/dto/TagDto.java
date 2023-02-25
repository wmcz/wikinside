package cz.cvut.fit.wikimetric.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.wikimetric.api.Views;

import java.util.Collection;

public record TagDto(@JsonView(Views.Incoming.class) String name,
                     @JsonView(Views.Incoming.class) Long id,
                     @JsonView(Views.Incoming.class) boolean assignable,
                     @JsonView(Views.Incoming.class) Long parentId,
                     @JsonView(Views.Outgoing.class) Collection<Long> childrenIds,
                     @JsonView(Views.Outgoing.class) Collection<Long> elementIds) {}
