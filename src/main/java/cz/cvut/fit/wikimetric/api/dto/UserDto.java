package cz.cvut.fit.wikimetric.api.dto;

import java.util.Collection;


public record UserDto(Long id,
                      String username,
                      String email,
                      Collection<Long> tagIds,
                      Collection<Long> eventIds) {}


