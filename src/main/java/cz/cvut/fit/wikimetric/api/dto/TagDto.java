package cz.cvut.fit.wikimetric.api.dto;

import java.util.Collection;

public record TagDto(String name,
                     Long id,
                     boolean assignable,
                     Long parentId,
                     Collection<Long> childrenIds,
                     Collection<Long> elementIds) {}
