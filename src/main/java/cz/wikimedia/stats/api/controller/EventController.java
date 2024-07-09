package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.api.controller.dto.EventDto;
import cz.wikimedia.stats.api.controller.dto.converter.EventConverter;
import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.RetrievalService;
import cz.wikimedia.stats.business.internal.RevisionService;
import cz.wikimedia.stats.model.Event;
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
    private final RetrievalService retrievalService;

    public EventController(EventService eventService, EventConverter eventConverter, RetrievalService retrievalService) {
        this.eventService = eventService;
        this.eventConverter = eventConverter;
        this.retrievalService = retrievalService;
    }

    @PostMapping("/events")
    EventDto create(@RequestBody EventDto event) {
        Event result = eventService
                .create(eventConverter.fromDto(event))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event already exists"));
        retrievalService.asyncRetrieve(result.getId());
        return eventConverter.toDto(result);
    }

    @GetMapping("/events/{id}")
    EventDto get(@PathVariable Long id) {
        return eventConverter.toDto(
                eventService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")));
    }

    @GetMapping("/events")
    Collection<EventDto> getMany() {
        Collection<EventDto> result = new ArrayList<>();
        eventService.findAll().forEach(e -> result.add(eventConverter.toDto(e)));
        return result;
    }

    @PutMapping("/events")
    EventDto update(@RequestBody EventDto event) {
        Event result = eventService
                .update(eventConverter.fromDto(event))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event does not exist"));
        retrievalService.asyncRetrieve(result.getId());
        return eventConverter.toDto(result);

    }

    @DeleteMapping("/events/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();

    }

}
