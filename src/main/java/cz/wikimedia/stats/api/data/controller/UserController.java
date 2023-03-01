package cz.wikimedia.stats.api.data.controller;

import cz.wikimedia.stats.api.dto.EventDto;
import cz.wikimedia.stats.api.dto.UserDto;
import cz.wikimedia.stats.api.dto.converter.EventConverter;
import cz.wikimedia.stats.api.dto.converter.UserConverter;
import cz.wikimedia.stats.business.UserService;
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

    @PostMapping("/users")
    public UserDto create(@RequestBody UserDto user) {
        return userConverter.toDto(
                userService
                        .create(userConverter.fromDto(user))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists")));
    }

    @GetMapping("/users/{id}")
    public UserDto get(@PathVariable Long id) {
        return userConverter.toDto(
                userService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    @GetMapping("/users")
    public Collection<UserDto> getMany() {
        Collection<UserDto> result = new ArrayList<>();
        userService.findAll().forEach(u -> result.add(userConverter.toDto(u)));
        return result;
    }

    @GetMapping("/users/username/{username}")
    public Collection<UserDto> getByName(@PathVariable String username) {
        return userConverter.toDto(userService.findByUsername(username));
    }

    @PutMapping("/users")
    public UserDto update(@RequestBody UserDto user) {
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
