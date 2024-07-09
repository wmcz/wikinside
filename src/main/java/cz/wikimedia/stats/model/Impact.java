package cz.wikimedia.stats.model;

public record Impact(Long createdPages,
                     Long editedPages,
                     Long edits,
                     Long byteDiff,
                     Long users,
                     Long events,
                     Long images,
                     Long usages) {}
