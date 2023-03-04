package cz.wikimedia.stats.api.controller;

import cz.wikimedia.stats.api.controller.dto.ProjectDto;
import cz.wikimedia.stats.api.controller.dto.converter.ProjectConverter;
import cz.wikimedia.stats.business.internal.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class ProjectController {
    private final ProjectConverter projectConverter;
    private final ProjectService projectService;

    public ProjectController(ProjectConverter projectConverter, ProjectService projectService) {
        this.projectConverter = projectConverter;
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    public ProjectDto create(@RequestBody ProjectDto project) {
        return projectConverter.toDto(
                projectService
                        .create(projectConverter.fromDto(project))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project already exists")));
    }

    @GetMapping("/projects/{id}")
    public ProjectDto get(@PathVariable Long id) {
        return projectConverter.toDto(
                projectService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found")));
    }

    @GetMapping("/projects")
    public Collection<ProjectDto> getMany() {
        Collection<ProjectDto> res = new ArrayList<>();
        projectService.findAll().forEach(p -> res.add(projectConverter.toDto(p)));
        return res;
    }

    @PutMapping("/projects")
    public ProjectDto update(@RequestBody ProjectDto project) {
        return projectConverter.toDto(
                projectService
                        .update(projectConverter.fromDto(project))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project does not exist")));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
