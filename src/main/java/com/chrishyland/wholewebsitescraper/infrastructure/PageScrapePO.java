package com.chrishyland.wholewebsitescraper.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scrapes")
public class PageScrapePO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapeId;
    @Column(columnDefinition = "TEXT")
    private String url;
    @Column(columnDefinition = "TEXT")
    private String html;
    private Instant updatedTime;
    private Instant scrapedTime;
}
