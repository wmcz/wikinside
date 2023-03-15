package cz.wikimedia.stats.api.controller.dto.converter;

import cz.wikimedia.stats.api.controller.dto.UserDto;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.UserTagService;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserConverter {

    private final UserTagService userTagService;
    private final EventService eventService;
    private final WmUserService wmUserService;

    public UserConverter(UserTagService userTagService, EventService eventService, WmUserService wmUserService) {
        this.userTagService = userTagService;
        this.eventService = eventService;
        this.wmUserService = wmUserService;
    }

    private UserDto toDtoInner(User user) {
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

    public UserDto toDto(User user) {
        user = user.setUsername(wmUserService.getUsername(user.getId()));
        return toDtoInner(user);
    }

    public Collection<UserDto> toDto(Collection<User> users) {
        users = wmUserService.updateNames(users);
        return users.stream().map(this::toDtoInner).toList();
    }

    public Collection<User> fromDto(Collection<UserDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }
}
