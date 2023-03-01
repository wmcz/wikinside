package cz.wikimedia.stats.api.dto.converter;

import cz.wikimedia.stats.api.dto.UserDto;
import cz.wikimedia.stats.business.EventService;
import cz.wikimedia.stats.business.UserTagService;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserConverter {

    private final UserTagService userTagService;
    private final EventService eventService;

    public UserConverter(UserTagService userTagService, EventService eventService) {
        this.userTagService = userTagService;
        this.eventService = eventService;
    }

    public UserDto toDto(User user) {
       return new UserDto(user.getId(),
                          user.getUsername(),
                          user.getEmail(),
                          ConverterUtils.getIds(user.getTags()),
                          ConverterUtils.getIds(user.getEvents()));
    }

    public User fromDto(UserDto dto) {
        return new User(dto.id(),
                        dto.username(),
                        dto.email(),
                        ConverterUtils.getElems(dto.tagIds(), userTagService),
                        ConverterUtils.getElems(dto.tagIds(), eventService));
    }

    public Collection<UserDto> toDto(Collection<User> users) {
        return users.stream().map(this::toDto).toList();
    }

    public Collection<User> fromDto(Collection<UserDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }
}
