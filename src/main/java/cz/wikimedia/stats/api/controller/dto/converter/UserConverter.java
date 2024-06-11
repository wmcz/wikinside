package cz.wikimedia.stats.api.controller.dto.converter;

import cz.wikimedia.stats.api.controller.dto.UserDto;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.business.internal.UserTagService;
import cz.wikimedia.stats.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserConverter {

    private final UserTagService userTagService;
    private final EventService eventService;
    private final WmUserService wmUserService;
    private final UserService userService;

    public UserConverter(UserTagService userTagService, EventService eventService, WmUserService wmUserService, UserService userService) {
        this.userTagService = userTagService;
        this.eventService = eventService;
        this.wmUserService = wmUserService;
        this.userService = userService;
    }

    private UserDto toDtoInner(User user) {
       return new UserDto(user.getId(),
                          user.getUsername(),
                          user.getRegistration(),
                          ConverterUtils.getIds(user.getInherentTags()),
                          ConverterUtils.getIds(user.getEventTags()),
                          ConverterUtils.getIds(user.getEvents()));
    }

    public User fromDto(UserDto dto) {
        User existing = dto.id() == null ? null : userService.findById(dto.id()).orElse(null);
        return new User(dto.id(),
                        existing == null ? null : existing.getLocalId(),
                        dto.username(),
                        existing == null ? null : existing.getRegistration(),
                        ConverterUtils.getElems(dto.inherentTagIds(), userTagService),
                        ConverterUtils.getElems(dto.eventIds(), eventService));
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
