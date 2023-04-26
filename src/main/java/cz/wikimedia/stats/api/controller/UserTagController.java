package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.api.controller.dto.TagDto;
import cz.wikimedia.stats.api.controller.dto.converter.TagConverter;
import cz.wikimedia.stats.business.internal.UserTagService;
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

    public UserTagController(UserTagService userTagService, TagConverter tagConverter) {
        this.userTagService = userTagService;
        this.tagConverter = tagConverter;
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
    public Collection<TagDto> getMany(@RequestBody(required = false) Collection<Long> ids) {
        Collection<TagDto> result = new ArrayList<>();
        if (ids == null)
            userTagService.findAll().forEach(t -> result.add(tagConverter.toDto(t)));
        else {
            ids.forEach(t -> result.add(
                    tagConverter.toDto(
                            userTagService
                                    .findById(t)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found")))));
        }
        return result;
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
