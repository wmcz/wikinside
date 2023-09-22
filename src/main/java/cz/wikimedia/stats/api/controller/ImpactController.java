package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.EventTagService;
import cz.wikimedia.stats.business.internal.ImpactService;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.EventTag;
import cz.wikimedia.stats.model.Impact;
import cz.wikimedia.stats.model.User;
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
    private final ImpactService impactService;

    public ImpactController(EventService eventService, EventTagService eventTagService, UserService userService, ImpactService impactService) {
        this.eventService = eventService;
        this.eventTagService = eventTagService;
        this.userService = userService;
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

}
