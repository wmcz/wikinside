package cz.wikimedia.stats.api.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record WmImage(@JsonProperty("pageid") Long pageId,
                      @JsonProperty("ns") int namespace,
                      @JsonProperty("title") String title,
                      @JsonProperty("imagerepository") String imageRepository,
                      @JsonProperty("imageinfo") Collection<ImageInfo> imageInfo,
                      @JsonProperty("globalusage") Collection<GlobalUsage> globalUsage,
                      @JsonProperty("fileusage") Collection<WmPage> commonsUsage) {}
