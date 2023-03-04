package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.api.controller.dto.RevisionDto;
import cz.wikimedia.stats.api.controller.dto.converter.RevisionConverter;
import cz.wikimedia.stats.business.internal.EventService;
import cz.wikimedia.stats.business.internal.RevisionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class RevisionController {

    private final RevisionService revisionService;
    private final EventService eventService;
    private final RevisionConverter revisionConverter;

    public RevisionController(RevisionService revisionService, EventService eventService, RevisionConverter revisionConverter) {
        this.revisionService = revisionService;
        this.eventService = eventService;
        this.revisionConverter = revisionConverter;
    }

    @PostMapping("/revisions/")
    public Collection<RevisionDto> create(@RequestBody Collection<RevisionDto> dtos) {
        Collection<RevisionDto> res = new ArrayList<>(dtos.size());
        revisionConverter.fromDto(dtos).forEach(r -> revisionService.create(r).ifPresent(re -> res.add(revisionConverter.toDto(re))));
        return res;
    }

    @GetMapping("/revisions/events/{event}")
    public Collection<RevisionDto> getPossible(@PathVariable Long event) {
        //get revisions made in time of event by event's participants in event's projects
        return revisionConverter.toDto(
                revisionService.getPossible(
                        eventService
                                .findById(event)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event does not exist"))));
    }
}
