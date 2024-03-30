package com.chrishyland.wholewebsitescraper.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class SitemapEntry {
    private String url;
    private LocalDateTime updatedTime;
    private LocalDateTime checkedTime;
    private int scrape_id;
    private Boolean scrape_is_new;
}
