package com.chrishyland.wholewebsitescraper.domain.entity;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@Builder
@Getter
public class PageScrape {
    private long scrapeId;
    private String url;
    private String html;
    private Instant updatedTime;
    private Instant scrapedTime;
}
