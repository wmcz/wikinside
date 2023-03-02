package cz.wikimedia.stats.api.controller.dto;

import java.util.Collection;


public record UserDto(Long id,
                      String username,
                      String email,
                      Collection<Long> tagIds,
                      Collection<Long> eventIds) {}


