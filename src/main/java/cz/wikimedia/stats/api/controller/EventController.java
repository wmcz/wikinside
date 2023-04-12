package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.api.controller.dto.EventDto;
import cz.wikimedia.stats.api.controller.dto.converter.EventConverter;
import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.RevisionService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Impact;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class EventController {
    private final EventService eventService;
    private final EventConverter eventConverter;
    private final RevisionService revisionService;

    public EventController(EventService eventService, EventConverter eventConverter, RevisionService revisionService) {
        this.eventService = eventService;
        this.eventConverter = eventConverter;
        this.revisionService = revisionService;
    }

    @PostMapping("/events")
    EventDto create(@RequestBody EventDto event) {
        Event result = eventService
                .create(eventConverter.fromDto(event))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event already exists"));
        revisionService.asyncGenerateRevs(result);
        return eventConverter.toDto(result);
    }

    @GetMapping("/events/{id}")
    EventDto get(@PathVariable Long id) {
        return eventConverter.toDto(
                eventService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")));
    }

    @GetMapping("/events/{id}/impact")
    Impact getImpact(@PathVariable Long id) {
        Event event = eventService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        return eventService.getImpact(event);
    }


    @GetMapping("/events")
    Collection<EventDto> getMany() {
        Collection<EventDto> result = new ArrayList<>();
        eventService.findAll().forEach(e -> result.add(eventConverter.toDto(e)));
        return result;
    }

    @GetMapping("/events/name/{name}")
    Collection<EventDto> getByName(@PathVariable String name) {
        return eventConverter.toDto(eventService.findByName(name));
    }

    @PutMapping("/events")
    EventDto update(@RequestBody EventDto event) {
        Event result = eventService
                .update(eventConverter.fromDto(event))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event does not exist"));
        revisionService.asyncGenerateRevs(result);
        return eventConverter.toDto(result);

    }

    @DeleteMapping("/events/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();

    }

}
