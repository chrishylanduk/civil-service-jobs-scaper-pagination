package com.chrishyland.wholewebsitescraper.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder(toBuilder = true)
public class SitemapEntry {
    private long sitemapEntryId;
    private String url;
    private Instant updatedTime;
    private Instant checkedTime;
    private Long scrapeId;
    private Boolean scrapeIsNew;
}
