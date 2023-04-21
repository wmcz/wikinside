package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.EventTagService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.EventTag;
import cz.wikimedia.stats.model.Impact;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ImpactController {

    private final EventService eventService;
    private final EventTagService eventTagService;

    public ImpactController(EventService eventService, EventTagService eventTagService) {
        this.eventService = eventService;
        this.eventTagService = eventTagService;
    }

    @GetMapping("/events/{id}/impact")
    Impact getEventImpact(@PathVariable Long id) {
        Event event = eventService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        return eventService.getImpact(event);
    }

    @GetMapping("/tags/event-tags/{id}/impact")
    Impact getEventTagImpact(@PathVariable Long id) {
        EventTag tag = eventTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return eventService.getImpact(tag.getTagged());
    }
}
