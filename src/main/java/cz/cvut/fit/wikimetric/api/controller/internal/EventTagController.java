package cz.cvut.fit.wikimetric.api.controller.internal;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.wikimetric.api.Views;
import cz.cvut.fit.wikimetric.api.dto.EventDto;
import cz.cvut.fit.wikimetric.api.dto.TagDto;
import cz.cvut.fit.wikimetric.api.dto.converter.EventConverter;
import cz.cvut.fit.wikimetric.api.dto.converter.TagConverter;
import cz.cvut.fit.wikimetric.business.EventTagService;
import cz.cvut.fit.wikimetric.model.EventTag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

@RestController
public class EventTagController {

    private Collection<EventTag> getChildrenInner(Long id) {
        Collection<EventTag> children = eventTagService.findById(id).map(EventTag::getChildren).orElse(new HashSet<>());
        children.forEach(c -> children.addAll(getChildrenInner(c.getId())));
        return children;
    }

    private final EventTagService eventTagService;
    private final TagConverter tagConverter;
    private final EventConverter eventConverter;

    public EventTagController(EventTagService eventTagService, TagConverter tagConverter, EventConverter eventConverter) {
        this.eventTagService = eventTagService;
        this.tagConverter = tagConverter;
        this.eventConverter = eventConverter;
    }

    @JsonView(Views.Incoming.class)
    @PostMapping("/tags/event-tags")
    public TagDto create(@RequestBody TagDto tag) {
        return tagConverter.toDto(
                eventTagService
                        .create(tagConverter.toEventTag(tag))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists")));

    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("tags/event-tags/{id}")
    public TagDto get(@PathVariable Long id) {
        return tagConverter.toDto(eventTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found")));
    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("tags/event-tags")
    public Collection<TagDto> getAll() {
        Collection<TagDto> result = new ArrayList<>();
        eventTagService.findAll().forEach(t -> result.add(tagConverter.toDto(t)));
        return result;
    }

    @GetMapping("tags/event-tags/{id}/events")
    public Collection<EventDto> getEvents(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean withChildren) {
        if (withChildren) return eventConverter.toDto(
                eventTagService
                        .getEventsWithTag(
                                eventTagService
                                        .findById(id)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist"))));
        else return eventConverter.toDto(
                eventTagService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist"))
                        .getTagged());
    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("/tags/event-tags/name/{name}")
    public Collection<TagDto> getEventTagsByName(@PathVariable String name) {
        return tagConverter.toDto(eventTagService.findByName(name));
    }

    @PostMapping("tags/event-tags/{id}/events")
    public Collection<EventDto> addEvents(@PathVariable Long id, @RequestBody Collection<Long> eventIds) {
        try {
            return eventConverter.toDto(eventTagService.addEvents(
                    eventTagService
                            .findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist")),
                    eventIds));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist");
        }
    }

    @DeleteMapping("tags/event-tags/{id}/event")
    public Collection<EventDto> removeEvents(@PathVariable Long id, @RequestBody Collection<Long> eventIds) {
        try {
            return eventConverter.toDto(eventTagService.removeEvents(
                    eventTagService
                            .findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist")),
                    eventIds));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist");
        }

    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("tags/event-tags/{id}/children")
    public Collection<TagDto> getChildren(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean recursive) {
        Collection<EventTag> children = eventTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist"))
                .getChildren();
        if (recursive) {
            children.forEach(c -> children.addAll(getChildrenInner(c.getId())));
        }
        return tagConverter.toDto(children);
    }

    @JsonView(Views.Outgoing.class)
    @PostMapping("tags/event-tags/{id}/children")
    public Collection<TagDto> addChildren(@PathVariable Long id, @RequestBody Collection<Long> childrenIds) {
        try {
            return tagConverter.toDto(
                    eventTagService.addChildren(
                            eventTagService
                                    .findById(id)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist")),
                            childrenIds));

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist");
        }

    }

    @JsonView(Views.Outgoing.class)
    @DeleteMapping("tags/event-tags/{id}/children")
    public Collection<TagDto> removeChildren(@PathVariable Long id, @RequestBody Collection<Long> childrenIds) {
        try {
            return tagConverter.toDto(
                    eventTagService.removeChildren(
                            eventTagService
                                    .findById(id)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist")),
                            childrenIds));

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist");
        }

    }

    @JsonView(Views.Outgoing.class)
    @PutMapping("/tags/event-tags")
    public TagDto update(@JsonView(Views.Incoming.class) @RequestBody TagDto tag) {
        return tagConverter.toDto(
                eventTagService
                        .update(tagConverter.toEventTag(tag))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event does not exist")));
    }

    @DeleteMapping("/tags/event-tags/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        eventTagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
