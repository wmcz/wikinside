package cz.cvut.fit.wikimetric.api.controller.internal;

import cz.cvut.fit.wikimetric.business.UserService;
import cz.cvut.fit.wikimetric.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
public class UserController {

    private UserService userService;

    @PostMapping("/users")
    User create(@RequestBody User user) {
        return userService
                .create(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists"));
    }

    @GetMapping("/users/{id}")
    User get(@PathVariable Long id) {
        return userService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @GetMapping("/users")
    Collection<User> getMany() { //TODO: filters
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/users/{username}")
    Collection<User> getByName(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PutMapping("/users")
    User update(@RequestBody User user) {
        return userService
                        .update(user)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    @DeleteMapping("/users/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
