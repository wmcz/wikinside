package cz.cvut.fit.wikimetric.api.dto.converter;

import cz.cvut.fit.wikimetric.api.dto.TagDto;
import cz.cvut.fit.wikimetric.business.*;
import cz.cvut.fit.wikimetric.model.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

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

    private <T extends Tag<? extends IdAble<Long>>, S extends AbstractService<T, Long>> T getParent(Long id, S service) {
        if (id == null) return null;
        else            return service.findById(id).orElse(null);
    }

    public <T extends Tag<S>, S extends IdAble<Long>> TagDto toDto(T tag) {

        return new TagDto(tag.getName(),
                          tag.getId(),
                          tag.isAssignable(),
                          tag.getParent() == null ? null : tag.getParent().getId(),
                          ConverterUtils.getIds(tag.getChildren()),
                          ConverterUtils.getIds(tag.getTagged()));
    }

    public UserTag toUserTag(TagDto dto) {
        if (dto.id() == null) {
            return new UserTag(null,
                               dto.name(),
                               dto.assignable(),
                               null,
                               getParent(dto.parentId(), userTagService),
                               null);
        }
        else return new UserTag(dto.id(),
                                dto.name(),
                                dto.assignable(),
                                userTagService.findById(dto.id()).map(UserTag::getTagged).orElse(new HashSet<>()),
                                getParent(dto.parentId(), userTagService),
                                userTagService.findById(dto.id()).map(UserTag::getChildren).orElse(new HashSet<>()));
    }



    public EventTag toEventTag(TagDto dto) {
        if (dto.id() == null) {
            return new EventTag(null,
                    dto.name(),
                    dto.assignable(),
                    null,
                    getParent(dto.parentId(), eventTagService),
                    null);
        }
        else return new EventTag(dto.id(),
                            dto.name(),
                            dto.assignable(),
                            eventTagService.findById(dto.id()).map(EventTag::getTagged).orElse(new HashSet<>()),
                            getParent(dto.parentId(), eventTagService),
                            eventTagService.findById(dto.id()).map(EventTag::getChildren).orElse(new HashSet<>()));
    }

    public <T extends Tag<S>, S extends IdAble<Long>> Collection<TagDto> toDto(Collection<T> tags) {
        return tags.stream().map(this::toDto).toList();
    }
}
