package cz.wikimedia.stats.api.controller.dto.converter;

import cz.wikimedia.stats.api.controller.dto.TagDto;
import cz.wikimedia.stats.business.internal.*;
import cz.wikimedia.stats.model.EventTag;
import cz.wikimedia.stats.model.IdAble;
import cz.wikimedia.stats.model.Tag;
import cz.wikimedia.stats.model.UserTag;
import org.springframework.stereotype.Component;

import java.util.Collection;

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

    private <T extends Tag<? extends IdAble<Long>>, S extends InternalService<T, Long>> T getParent(Long id, S service) {
        if (id == null) return null;
        else            return service.findById(id).orElse(null);
    }

    public TagDto toDto(UserTag tag) {
        return toDto(tag, ConverterUtils.getIds(tag.getTagged()), ConverterUtils.getIds(tag.getEvents()));
    }

    public TagDto toDto(EventTag tag) {
        return toDto(tag, null, ConverterUtils.getIds(tag.getTagged()));
    }
    private <T extends Tag<S>, S extends IdAble<Long>> TagDto toDto(T tag, Collection<Long> users, Collection<Long> events) {

        return new TagDto(tag.getName(),
                          tag.getId(),
                          tag.getParent() == null ? null : tag.getParent().getId(),
                          tag.getColor(),
                          ConverterUtils.getIds(tag.getChildren()),
                          users,
                          events);
    }

    public UserTag toUserTag(TagDto dto) {
        return new UserTag(dto.id(),
                           dto.name(),
                           dto.color(),
                           ConverterUtils.getElems(dto.userIds(), userService),
                           ConverterUtils.getElems(dto.eventIds(), eventService),
                           getParent(dto.parentId(), userTagService),
                           ConverterUtils.getElems(dto.childrenIds(), userTagService));
    }



    public EventTag toEventTag(TagDto dto) {
        return new EventTag(dto.id(),
                            dto.name(),
                            dto.color(),
                            ConverterUtils.getElems(dto.eventIds(), eventService),
                            getParent(dto.parentId(), eventTagService),
                            ConverterUtils.getElems(dto.childrenIds(), eventTagService));
    }
}
