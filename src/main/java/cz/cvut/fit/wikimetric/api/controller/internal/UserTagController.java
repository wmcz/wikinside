package cz.cvut.fit.wikimetric.api.controller.internal;

import cz.cvut.fit.wikimetric.api.dto.TagDto;
import cz.cvut.fit.wikimetric.api.dto.UserDto;
import cz.cvut.fit.wikimetric.api.dto.converter.TagConverter;
import cz.cvut.fit.wikimetric.api.dto.converter.UserConverter;
import cz.cvut.fit.wikimetric.business.UserTagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class UserTagController {

    private final UserTagService userTagService;
    private final TagConverter tagConverter;
    private final UserConverter userConverter;

    public UserTagController(UserTagService userTagService, TagConverter tagConverter, UserConverter userConverter) {
        this.userTagService = userTagService;
        this.tagConverter = tagConverter;
        this.userConverter = userConverter;
    }

    @PostMapping("/tags/user-tags")
    public TagDto create(@RequestBody TagDto tag) {
        return tagConverter.toDto(
                userTagService
                        .create(tagConverter.toUserTag(tag))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists")));

    }

    @GetMapping("tags/user-tags/{id}")
    public TagDto get(@PathVariable Long id) {
        return tagConverter.toDto(
                userTagService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found")));
    }

    @GetMapping("tags/user-tags")
    public Collection<TagDto> getAll() {
        Collection<TagDto> result = new ArrayList<>();
        userTagService.findAll().forEach(t -> result.add(tagConverter.toDto(t)));
        return result;
    }

    @GetMapping("tags/user-tags/{id}/users")
    public Collection<UserDto> getUsers(@PathVariable Long id) {
        //includes children tags' users
        return userConverter.toDto(
                userTagService
                        .getUsersWithTag(
                                userTagService
                                        .findById(id)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag does not exist"))));


    }

    @GetMapping("/tags/user-tags/name/{name}")
    public Collection<TagDto> getUserTagsByName(@PathVariable String name) {
        return tagConverter.toDto(userTagService.findByName(name));
    }

    @PutMapping("/tags/user-tags")
    public TagDto update(@RequestBody TagDto tag) {
        return tagConverter.toDto(
                userTagService
                        .update(tagConverter.toUserTag(tag))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")));
    }

    @DeleteMapping("/tags/user-tags/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userTagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
