package cz.wikimedia.stats.api.controller.dto.converter;

import cz.wikimedia.stats.api.controller.dto.EventDto;
import cz.wikimedia.stats.business.internal.*;
import cz.wikimedia.stats.model.Event;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class EventConverter {

    private final EventTagService eventTagService;
    private final UserService userService;
    private final ProjectService projectService;
    private final EventService eventService;

    public EventConverter(EventTagService eventTagService, UserService userService, ProjectService projectService, EventService eventService) {
        this.eventTagService = eventTagService;
        this.userService = userService;
        this.projectService = projectService;
        this.eventService = eventService;
    }

    public EventDto toDto(Event event) {
        return new EventDto(event.getId(),
                            ConverterUtils.getIds(event.getTags()),
                            event.getName(),
                            event.getCategory(),
                            event.getStartDate(),
                            event.getEndDate(),
                            ConverterUtils.getIds(event.getParticipants()),
                            ConverterUtils.getIds(event.getProjects()));
    }

    public Event fromDto(EventDto dto) {
        return new Event(dto.id(),
                         ConverterUtils.getElems(dto.tagIds(), eventTagService),
                         dto.name(),
                         dto.hashtag(),
                         dto.startDate(),
                         dto.endDate(),
                         ConverterUtils.getElems(dto.userIds(), userService),
                         ConverterUtils.getElems(dto.projectIds(), projectService),
                         dto.id() == null ? new HashSet<>() : eventService.findById(dto.id()).map(Event::getRevisions).orElse(new HashSet<>()));
    }

    public Collection<EventDto> toDto(Collection<Event> events) {
        return events.stream().map(this::toDto).toList();
    }

    public Collection<Event> fromDto(Collection<EventDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }
}
