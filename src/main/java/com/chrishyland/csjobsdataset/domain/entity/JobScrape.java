package com.chrishyland.csjobsdataset.domain.entity;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Builder
@Getter
public class JobScrape {
    private int jcode;
    private String html;
    private LocalDateTime updatedTime;
    private LocalDateTime scrapedTime;
}
