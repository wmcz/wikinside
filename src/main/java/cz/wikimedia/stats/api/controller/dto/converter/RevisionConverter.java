package cz.wikimedia.stats.api.controller.dto.converter;

import cz.wikimedia.stats.api.controller.dto.RevisionDto;
import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.ProjectService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Revision;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RevisionConverter {
    private final UserService userService;
    private final EventService eventService;
    private final ProjectService projectService;

    public RevisionConverter(UserService userService, EventService eventService, ProjectService projectService) {
        this.userService = userService;
        this.eventService = eventService;
        this.projectService = projectService;
    }

    public Revision fromDto(RevisionDto dto) {
        return new Revision(
                dto.id(),
                dto.revId(),
                dto.diff(),
                dto.pageId(),
                dto.isCreation(),
                ConverterUtils.getIfNotNull(dto.userId(),    userService),
                ConverterUtils.getElems    (dto.eventIds(),  eventService),
                ConverterUtils.getIfNotNull(dto.projectId(), projectService),
                dto.timestamp(),
                dto.summary());
    }

    public RevisionDto toDto(Revision revision) {
        return new RevisionDto(
                revision.getId(),
                revision.getRevId(),
                revision.getDiff(),
                revision.getPageId(),
                revision.isPageCreation(),
                ConverterUtils.getIfNotNull(revision.getUser()),
                ConverterUtils.getIds      (revision.getEvents()),
                ConverterUtils.getIfNotNull(revision.getProject()),
                revision.getTimestamp(),
                revision.getSummary());
    }

    public Collection<Revision> fromDto(Collection<RevisionDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public Collection<RevisionDto> toDto(Collection<Revision> revisions) {
        return revisions.stream().map(this::toDto).toList();
    }
}
