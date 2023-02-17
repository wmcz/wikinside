package cz.cvut.fit.wikimetric.api.dto.converter;

import cz.cvut.fit.wikimetric.api.dto.TagDto;
import cz.cvut.fit.wikimetric.business.*;
import cz.cvut.fit.wikimetric.model.*;
import org.springframework.stereotype.Component;

@Component
public class TagConverter {
    private final UserTagService userTagService;
    private final EventService eventService;
    private final UserService userService;
    private final EventTagService eventTagService;

    public TagConverter(UserService userService, UserTagService userTagService, EventService eventService, EventTagService eventTagService) {
        this.userService = userService;
        this.userTagService = userTagService;
        this.eventService = eventService;
        this.eventTagService = eventTagService;
    }

    public TagDto toDto(Tag<IdAble<Long>> tag) {

        return new TagDto(tag.getName(),
                          tag.getId(),
                          tag.isAssignable(),
                          tag.getParent().getId(),
                          ConverterUtils.getIds(tag.getChildren()),
                          ConverterUtils.getIds(tag.getTagged()));
    }

    public UserTag toUserTag(TagDto dto) {
        return new UserTag(dto.id(),
                           dto.name(),
                           dto.assignable(),
                           ConverterUtils.getElems(dto.elementIds(), userService),
                           userTagService.findById(dto.parentId()).orElse(null),
                           ConverterUtils.getElems(dto.childrenIds(), userTagService));
    }

    public EventTag toEventTag(TagDto dto) {
        return new EventTag(dto.id(),
                            dto.name(),
                            dto.assignable(),
                            ConverterUtils.getElems(dto.elementIds(), eventService),
                            eventTagService.findById(dto.parentId()).orElse(null),
                            ConverterUtils.getElems(dto.childrenIds(), eventTagService));
    }
}
