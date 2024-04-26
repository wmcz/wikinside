package cz.wikimedia.stats.api.controller.dto;

import java.time.LocalDate;
import java.util.Collection;

public record EventDto(Long id,
                       Collection<Long> tagIds,
                       String name,
                       String strat,
                       String category,
                       LocalDate startDate,
                       LocalDate endDate,
                       Collection<Long> userIds,
                       Collection<Long> projectIds) {}
