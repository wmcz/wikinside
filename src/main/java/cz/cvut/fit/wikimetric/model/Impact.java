package cz.cvut.fit.wikimetric.model;

abstract class Impact {


    private final int createdPages;
    private final int editedPages;
    private final int edits;
    private final int uploadedFiles;
    private final int pageViews;

    public Impact(int createdPages, int editedPages, int edits, int uploadedFiles, int pageViews) {
        this.createdPages = createdPages;
        this.editedPages = editedPages;
        this.edits = edits;
        this.uploadedFiles = uploadedFiles;
        this.pageViews = pageViews;
    }

    public Impact() {

        createdPages = 0;
        editedPages = 0;
        edits = 0;
        uploadedFiles = 0;
        pageViews = 0;
    }

}
