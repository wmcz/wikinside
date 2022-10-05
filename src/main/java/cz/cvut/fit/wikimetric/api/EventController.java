package cz.cvut.fit.wikimetric.api;

import cz.cvut.fit.wikimetric.dto.EventDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class EventController {

    @PostMapping("/events")
    EventDto create(@RequestBody EventDto event) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/events/{id}")
    EventDto get(@PathVariable Long id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/events")
    EventDto getMany() { //TODO: filters
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/events}")
    EventDto update(@RequestBody EventDto user) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/events/{id}")
    void delete(@PathVariable Long id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

}
