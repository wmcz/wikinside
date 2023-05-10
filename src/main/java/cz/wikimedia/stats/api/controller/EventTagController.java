package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.api.controller.dto.TagDto;
import cz.wikimedia.stats.api.controller.dto.converter.TagConverter;
import cz.wikimedia.stats.business.internal.EventTagService;
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

    public EventTagController(EventTagService eventTagService, TagConverter tagConverter) {
        this.eventTagService = eventTagService;
        this.tagConverter = tagConverter;
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
    public Collection<TagDto> getMany(@RequestBody(required = false) Collection<Long> ids) {
        Collection<TagDto> result = new ArrayList<>();
        if (ids == null)
            eventTagService.findAll().forEach(t -> result.add(tagConverter.toDto(t)));
        else {
            ids.forEach(t -> result.add(
                    tagConverter.toDto(
                            eventTagService
                                    .findById(t)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found")))));
        }
        return result;
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
