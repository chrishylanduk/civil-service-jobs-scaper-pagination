package com.chrishyland.wholewebsitescraper.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface PageScrapePOJPARepository extends JpaRepository<PageScrapePO, Long> {
    PageScrapePO findTopByUrlAndUpdatedTimeOrderByScrapeIdDesc(String url, Instant updatedTime);
}