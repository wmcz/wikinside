package cz.cvut.fit.wikimetric.api.controller.internal;

import cz.cvut.fit.wikimetric.api.dto.EventDto;
import cz.cvut.fit.wikimetric.api.dto.converter.EventConverter;
import cz.cvut.fit.wikimetric.business.EventService;
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

    public EventController(EventService eventService, EventConverter eventConverter) {
        this.eventService = eventService;
        this.eventConverter = eventConverter;
    }

    @PostMapping("/events")
    EventDto create(@RequestBody EventDto event) {
        return eventConverter.toDto(
                eventService
                        .create(eventConverter.fromDto(event))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event already exists")));
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

    @GetMapping("/events/name/{name}")
    Collection<EventDto> getByName(@PathVariable String name) {
        return eventConverter.toDto(eventService.findByName(name));
    }

    @PutMapping("/events")
    EventDto update(@RequestBody EventDto event) {
        return eventConverter.toDto(
                eventService
                        .update(eventConverter.fromDto(event))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event does not exist")));
    }

    @DeleteMapping("/events/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();

    }

}
