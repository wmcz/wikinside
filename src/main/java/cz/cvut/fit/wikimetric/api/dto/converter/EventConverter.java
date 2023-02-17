package cz.cvut.fit.wikimetric.api.dto.converter;

import cz.cvut.fit.wikimetric.api.dto.EventDto;
import cz.cvut.fit.wikimetric.business.EventTagService;
import cz.cvut.fit.wikimetric.business.UserService;
import cz.cvut.fit.wikimetric.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

    private final EventTagService eventTagService;
    private final UserService userService;

    public EventConverter(EventTagService eventTagService, UserService userService) {
        this.eventTagService = eventTagService;
        this.userService = userService;
    }

    public EventDto toDto(Event event) {
        return new EventDto(event.getId(),
                            ConverterUtils.getIds(event.getTags()),
                            event.getName(),
                            event.getStartDate(),
                            event.getEndDate(),
                            ConverterUtils.getIds(event.getParticipants()));
    }

    public Event fromDto(EventDto dto) {
        return new Event(dto.id(),
                         ConverterUtils.getElems(dto.tagIds(), eventTagService),
                         dto.name(),
                         dto.startDate(),
                         dto.endDate(),
                         ConverterUtils.getElems(dto.userIds(), userService));
    }
}
