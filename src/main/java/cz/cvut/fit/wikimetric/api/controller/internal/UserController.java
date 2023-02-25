package cz.cvut.fit.wikimetric.api.controller.internal;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.wikimetric.api.Views;
import cz.cvut.fit.wikimetric.api.dto.EventDto;
import cz.cvut.fit.wikimetric.api.dto.UserDto;
import cz.cvut.fit.wikimetric.api.dto.converter.EventConverter;
import cz.cvut.fit.wikimetric.api.dto.converter.UserConverter;
import cz.cvut.fit.wikimetric.business.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final EventConverter eventConverter;

    public UserController(UserService userService, UserConverter userConverter, EventConverter eventConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.eventConverter = eventConverter;
    }

    @JsonView(Views.Outgoing.class)
    @PostMapping("/users")
    public UserDto create(@JsonView(Views.Incoming.class) @RequestBody UserDto user) {
        return userConverter.toDto(
                userService
                        .create(userConverter.fromDto(user))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists")));
    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("/users/{id}")
    public UserDto get(@PathVariable Long id) {
        return userConverter.toDto(
                userService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("/users")
    public Collection<UserDto> getMany() {
        Collection<UserDto> result = new ArrayList<>();
        userService.findAll().forEach(u -> result.add(userConverter.toDto(u)));
        return result;
    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("/users/username/{username}")
    public Collection<UserDto> getByName(@PathVariable String username) {
        return userConverter.toDto(userService.findByUsername(username));
    }

    @JsonView(Views.Outgoing.class)
    @PutMapping("/users")
    public UserDto update(@JsonView(Views.Incoming.class) @RequestBody UserDto user) {
        return userConverter.toDto(
                userService
                        .update(userConverter.fromDto(user))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")));
    }

    @PostMapping("/users/{id}/events")
    public Collection<EventDto> addEvents(@PathVariable Long id, @RequestBody Collection<Long> eventIds) {
        try {
            return eventConverter.toDto(
                    userService.addEvents(
                            userService
                                    .findById(id)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")),
                            eventIds));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    @DeleteMapping("/users/{id}/events")
    public Collection<EventDto> removeEvents(@PathVariable Long id, @RequestBody Collection<Long> eventIds) {
        try {
            return eventConverter.toDto(
                    userService.removeEvents(
                            userService
                                    .findById(id)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")),
                            eventIds));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
