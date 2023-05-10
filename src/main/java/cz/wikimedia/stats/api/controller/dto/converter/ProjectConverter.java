package cz.wikimedia.stats.api.controller.dto.converter;

import cz.wikimedia.stats.api.controller.dto.ProjectDto;
import cz.wikimedia.stats.model.Project;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ProjectConverter {
    public Project fromDto(ProjectDto dto) {
        return new Project(
                dto.id(),
                dto.name(),
                dto.path());
    }

    public ProjectDto toDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getPath());
    }

    public Collection<Project> fromDto(Collection<ProjectDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public Collection<ProjectDto> toDto(Collection<Project> projects) {
        return projects.stream().map(this::toDto).toList();
    }
}
