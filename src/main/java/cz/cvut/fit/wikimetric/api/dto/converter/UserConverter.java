package cz.cvut.fit.wikimetric.api.dto.converter;

import cz.cvut.fit.wikimetric.api.dto.UserDto;
import cz.cvut.fit.wikimetric.business.EventService;
import cz.cvut.fit.wikimetric.business.UserTagService;
import cz.cvut.fit.wikimetric.model.User;
import org.springframework.stereotype.Component;

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
}
