package com.chrishyland.csjobsdataset.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class SitemapEntry {
    private int jcode;
    private LocalDateTime updatedTime;
    private LocalDateTime checkedTime;
    private int scrapeid;
    private Boolean scrape_is_new;
}
