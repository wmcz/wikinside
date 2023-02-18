package cz.cvut.fit.wikimetric.api.controller.internal;

import cz.cvut.fit.wikimetric.api.dto.UserDto;
import cz.cvut.fit.wikimetric.api.dto.converter.UserConverter;
import cz.cvut.fit.wikimetric.business.UserService;
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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
