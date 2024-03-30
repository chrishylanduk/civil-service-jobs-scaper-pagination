package com.chrishyland.wholewebsitescraper.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scrapes")
public class PageScrapePO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrape_id;
    private String url;
    private String html;
    private LocalDateTime updatedTime;
    private LocalDateTime scrapedTime;
}
