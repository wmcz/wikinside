package cz.cvut.fit.wikimetric.api.controller.internal;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.wikimetric.api.Views;
import cz.cvut.fit.wikimetric.api.dto.TagDto;
import cz.cvut.fit.wikimetric.api.dto.UserDto;
import cz.cvut.fit.wikimetric.api.dto.converter.TagConverter;
import cz.cvut.fit.wikimetric.api.dto.converter.UserConverter;
import cz.cvut.fit.wikimetric.business.UserTagService;
import cz.cvut.fit.wikimetric.model.UserTag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

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

    private Collection<UserTag> getChildrenInner(Long id) {
        Collection<UserTag> children = userTagService.findById(id).map(UserTag::getChildren).orElse(new HashSet<>());
        children.forEach(c -> children.addAll(getChildrenInner(c.getId())));
        return children;
    }

    @JsonView(Views.Outgoing.class)
    @PostMapping("/tags/user-tags")
    public TagDto create(@JsonView(Views.Incoming.class) @RequestBody TagDto tag) {
        return tagConverter.toDto(
                userTagService
                        .create(tagConverter.toUserTag(tag))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists")));

    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("tags/user-tags/{id}")
    public TagDto get(@PathVariable Long id) {
        return tagConverter.toDto(
                userTagService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found")));
    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("tags/user-tags")
    public Collection<TagDto> getAll() {
        Collection<TagDto> result = new ArrayList<>();
        userTagService.findAll().forEach(t -> result.add(tagConverter.toDto(t)));
        return result;
    }

    @GetMapping("tags/user-tags/{id}/users")
    public Collection<UserDto> getUsers(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean withChildren) {
        if (withChildren) return userConverter.toDto(
                userTagService.getUsersWithTag(
                        userTagService
                                .findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist"))));
        else return userConverter.toDto(
                userTagService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist"))
                        .getTagged());
    }

    @PostMapping("tags/user-tags/{id}/users")
    public Collection<UserDto> addUsers(@PathVariable Long id, @RequestBody Collection<Long> userIds) {
        try {
            return userConverter.toDto(userTagService.addUsers(
                    userTagService
                            .findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist")),
                    userIds));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist");
        }
    }

    @DeleteMapping("tags/user-tags/{id}/users")
    public Collection<UserDto> removeUsers(@PathVariable Long id, @RequestBody Collection<Long> userIds) {
        try {
            return userConverter.toDto(userTagService.removeUsers(
                    userTagService
                            .findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist")),
                    userIds));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist");
        }

    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("tags/user-tags/{id}/children")
    public Collection<TagDto> getChildren(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean recursive) {
        Collection<UserTag> children = userTagService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist"))
                .getChildren();
        if (recursive) {
            children.forEach(c -> children.addAll(getChildrenInner(c.getId())));
        }
        return tagConverter.toDto(children);
    }

    @JsonView(Views.Outgoing.class)
    @PostMapping("tags/user-tags/{id}/children")
    public Collection<TagDto> addChildren(@PathVariable Long id, @RequestBody Collection<Long> childrenIds) {
        try {
            return tagConverter.toDto(
                    userTagService.addChildren(
                            userTagService
                                    .findById(id)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist")),
                            childrenIds));

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist");
        }

    }

    @JsonView(Views.Outgoing.class)
    @DeleteMapping("tags/user-tags/{id}/children")
    public Collection<TagDto> removeChildren(@PathVariable Long id, @RequestBody Collection<Long> childrenIds) {
        try {
            return tagConverter.toDto(
                    userTagService.removeChildren(
                            userTagService
                                    .findById(id)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist")),
                            childrenIds));

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exist");
        }

    }

    @JsonView(Views.Outgoing.class)
    @GetMapping("/tags/user-tags/name/{name}")
    public Collection<TagDto> getUserTagsByName(@PathVariable String name) {
        return tagConverter.toDto(userTagService.findByName(name));
    }

    @JsonView(Views.Outgoing.class)
    @PutMapping("/tags/user-tags")
    public TagDto update(@JsonView(Views.Incoming.class) @RequestBody TagDto tag) {
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
