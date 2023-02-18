package cz.cvut.fit.wikimetric.api.controller.internal;

import cz.cvut.fit.wikimetric.api.dto.EventDto;
import cz.cvut.fit.wikimetric.api.dto.TagDto;
import cz.cvut.fit.wikimetric.api.dto.converter.EventConverter;
import cz.cvut.fit.wikimetric.api.dto.converter.TagConverter;
import cz.cvut.fit.wikimetric.business.EventTagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class EventTagController {

    private final EventTagService eventTagService;
    private final TagConverter tagConverter;
    private final EventConverter eventConverter;

    public EventTagController(EventTagService eventTagService, TagConverter tagConverter, EventConverter eventConverter) {
        this.eventTagService = eventTagService;
        this.tagConverter = tagConverter;
        this.eventConverter = eventConverter;
    }

    @PostMapping("/tags/event-tags")
    public TagDto create(@RequestBody TagDto tag) {
        return tagConverter.toDto(
                eventTagService
                        .create(tagConverter.toEventTag(tag))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists")));

    }

    @GetMapping("tags/event-tags/{id}")
    public TagDto get(@PathVariable Long id) {
        return tagConverter.toDto(eventTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found")));
    }

    @GetMapping("tags/event-tags")
    public Collection<TagDto> getAll() {
        Collection<TagDto> result = new ArrayList<>();
        eventTagService.findAll().forEach(t -> result.add(tagConverter.toDto(t)));
        return result;
    }

    @GetMapping("tags/event-tags/{id}/events")
    public Collection<EventDto> getEvents(@PathVariable Long id) {
        return eventConverter.toDto(
                eventTagService
                        .getEventsWithTag(
                                eventTagService
                                        .findById(id)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag does not exist"))));


    }

    @GetMapping("/tags/event-tags/name/{name}")
    public Collection<TagDto> getEventTagsByName(@PathVariable String name) {
        return tagConverter.toDto(eventTagService.findByName(name));
    }

    @PutMapping("/tags/event-tags")
    public TagDto update(@RequestBody TagDto tag) {
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
