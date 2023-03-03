package cz.wikimedia.stats.api.controller.dto.converter;

import cz.wikimedia.stats.api.controller.dto.EventDto;
import cz.wikimedia.stats.business.internal.EventTagService;
import cz.wikimedia.stats.business.internal.ProjectService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Event;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class EventConverter {

    private final EventTagService eventTagService;
    private final UserService userService;
    private final ProjectService projectService;

    public EventConverter(EventTagService eventTagService, UserService userService, ProjectService projectService) {
        this.eventTagService = eventTagService;
        this.userService = userService;
        this.projectService = projectService;
    }

    public EventDto toDto(Event event) {
        return new EventDto(event.getId(),
                            ConverterUtils.getIds(event.getTags()),
                            event.getName(),
                            event.getStartDate(),
                            event.getEndDate(),
                            ConverterUtils.getIds(event.getParticipants()),
                            ConverterUtils.getIds(event.getProjects()));
    }

    public Event fromDto(EventDto dto) {
        return new Event(dto.id(),
                         ConverterUtils.getElems(dto.tagIds(), eventTagService),
                         dto.name(),
                         dto.startDate(),
                         dto.endDate(),
                         ConverterUtils.getElems(dto.userIds(), userService),
                         ConverterUtils.getElems(dto.projectIds(), projectService));
    }

    public Collection<EventDto> toDto(Collection<Event> events) {
        return events.stream().map(this::toDto).toList();
    }

    public Collection<Event> fromDto(Collection<EventDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }
}
