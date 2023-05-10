package cz.wikimedia.stats.api.controller.dto;

import java.util.Collection;

public record TagDto(String name,
                     Long id,
                     Long parentId,
                     Collection<Long> childrenIds,
                     Collection<Long> elementIds) {}
