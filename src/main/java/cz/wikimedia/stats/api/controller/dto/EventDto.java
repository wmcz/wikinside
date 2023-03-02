package cz.wikimedia.stats.api.controller.dto;

import java.time.Instant;
import java.util.Collection;

public record EventDto(Long id,
                       Collection<Long> tagIds,
                       String name,
                       Instant startDate,
                       Instant endDate,
                       Collection<Long> userIds) {}
