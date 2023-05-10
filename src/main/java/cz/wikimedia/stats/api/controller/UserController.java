package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.api.controller.dto.UserDto;
import cz.wikimedia.stats.api.controller.dto.converter.UserConverter;
import cz.wikimedia.stats.business.internal.UserService;
import cz.wikimedia.stats.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @PostMapping("/users")
    public UserDto create(@RequestBody UserDto user) {
        return userConverter.toDto(
                userService
                        .createFromGlobalUser(userConverter.fromDto(user))
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
        Collection<User> list = new ArrayList<>();
        userService.findAll().forEach(list::add);
        return userConverter.toDto(list);
    }

    @PutMapping("/users")
    public UserDto update(@RequestBody UserDto user) {
        return userConverter.toDto(
                userService
                        .update(userConverter.fromDto(user))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
