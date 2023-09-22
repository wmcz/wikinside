package cz.wikimedia.stats.api.controller.dto;

import java.time.Instant;
import java.util.Collection;


public record UserDto(Long id,
                      String username,
                      Instant registration,
                      Collection<Long> tagIds,
                      Collection<Long> eventIds) {}


