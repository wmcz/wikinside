package cz.cvut.fit.wikimetric.api.controller.internal;

import cz.cvut.fit.wikimetric.business.UserTagService;
import cz.cvut.fit.wikimetric.model.User;
import cz.cvut.fit.wikimetric.model.UserTag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RestController
public class UserTagController {

    private UserTagService userTagService;

    @PostMapping("/tags/user-tags")
    UserTag create(@RequestBody UserTag tag) {
        return userTagService
                .create(tag)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists"));

    }

    @GetMapping("tags/user-tags/{id}")
    UserTag get(@PathVariable Long id) {
        return userTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
    }

    @GetMapping("tags/user-tags")
    Collection<UserTag> getAll() {
        Collection<UserTag> result = new ArrayList<>();
        userTagService
                .findAll()
                .forEach(result::add);
        return result;
    }

    @GetMapping("tags/user-tags/{id}/users")
    Collection<User> getUsers(@PathVariable Long id) {
        return userTagService
                .getUsersWithTags(
                        Collections.singleton(
                                userTagService
                                        .findById(id)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag does not exist"))));


    }

    @PutMapping("/tags/user-tags")
    UserTag update(@RequestBody UserTag tag) {
        return userTagService
                .update(tag)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    @DeleteMapping("/tags/user-tags/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        userTagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
