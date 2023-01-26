package cz.cvut.fit.wikimetric.api.controller.internal;

import cz.cvut.fit.wikimetric.business.EventTagService;
import cz.cvut.fit.wikimetric.model.Event;
import cz.cvut.fit.wikimetric.model.EventTag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RestController
public class EventTagController {

    private EventTagService eventTagService;

    @PostMapping("/tags/event-tags")
    EventTag create(@RequestBody EventTag tag) {
        return eventTagService
                .create(tag)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists"));

    }

    @GetMapping("tags/event-tags/{id}")
    EventTag get(@PathVariable Long id) {
        return eventTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
    }

    @GetMapping("tags/event-tags")
    Collection<EventTag> getAll() {
        Collection<EventTag> result = new ArrayList<>();
        eventTagService
                .findAll()
                .forEach(result::add);
        return result;
    }

    @GetMapping("tags/event-tags/{id}/events")
    Collection<Event> getEvents(@PathVariable Long id) {
        return eventTagService
                .getEventsWithTags(
                        Collections.singleton(
                                eventTagService
                                        .findById(id)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag does not exist"))));


    }

    @PutMapping("/tags/event-tags")
    EventTag update(@RequestBody EventTag tag) {
        return eventTagService
                .update(tag)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event does not exist"));
    }

    @DeleteMapping("/tags/event-tags/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        eventTagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
