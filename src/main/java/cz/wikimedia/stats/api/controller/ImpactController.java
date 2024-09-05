package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.business.internal.*;
import cz.wikimedia.stats.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ImpactController {

    private final EventService eventService;
    private final EventTagService eventTagService;
    private final UserService userService;
    private final UserTagService userTagService;
    private final ImpactService impactService;

    public ImpactController(EventService eventService, EventTagService eventTagService, UserService userService, UserTagService userTagService, ImpactService impactService) {
        this.eventService = eventService;
        this.eventTagService = eventTagService;
        this.userService = userService;
        this.userTagService = userTagService;
        this.impactService = impactService;
    }

    @GetMapping("/events/{id}/impact")
    Impact getEventImpact(@PathVariable Long id) {
        Event event = eventService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        return impactService.getImpact(event);
    }

    @GetMapping("/tags/event-tags/{id}/impact")
    Impact getEventTagImpact(@PathVariable Long id) {
        EventTag tag = eventTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return impactService.getImpact(tag);
    }

    @GetMapping("/users/{id}/impact")
    Impact getUserImpact(@PathVariable Long id) {
        User user = userService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return impactService.getImpact(user);
    }

    @GetMapping("/tags/user-tags/{id}/impact")
    Impact getUserTagImpact(@PathVariable Long id) {
        UserTag tag = userTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return impactService.getImpact(tag);
    }

}
