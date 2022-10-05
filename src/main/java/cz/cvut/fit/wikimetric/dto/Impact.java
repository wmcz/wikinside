package cz.cvut.fit.wikimetric.dto;

public record Impact(
        int createdPages,
        int editedPages,
        int edits,
        int uploadedFiles,
        int pageViews) {
}
