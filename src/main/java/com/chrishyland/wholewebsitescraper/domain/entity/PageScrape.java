package com.chrishyland.wholewebsitescraper.domain.entity;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Builder
@Getter
public class PageScrape {
    private String url;
    private String html;
    private LocalDateTime updatedTime;
    private LocalDateTime scrapedTime;
}
