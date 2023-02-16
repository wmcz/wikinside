package cz.cvut.fit.wikimetric.api.controller.internal;

import cz.cvut.fit.wikimetric.business.EventService;
import cz.cvut.fit.wikimetric.model.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/events")
    Event create(@RequestBody Event event) {
        return eventService
                .create(event)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event already exists"));
    }

    @GetMapping("/events/{id}")
    Event get(@PathVariable Long id) {
        return eventService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @GetMapping("/events")
    Collection<Event> getMany() {
        Collection<Event> result = new ArrayList<>();
        eventService.findAll().forEach(result::add);
        return result;
    }

    @GetMapping("/events/{name}")
    Collection<Event> getByName(@PathVariable String name) {
        return eventService.findByName(name);
    }

    @PutMapping("/events")
    Event update(@RequestBody Event event) {
        return eventService
                .update(event)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event does not exist"));
    }

    @DeleteMapping("/events/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();

    }

}
