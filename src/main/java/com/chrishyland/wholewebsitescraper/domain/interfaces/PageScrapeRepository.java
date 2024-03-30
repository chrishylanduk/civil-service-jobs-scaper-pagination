package com.chrishyland.wholewebsitescraper.domain.interfaces;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;

import java.time.Instant;
import java.util.Optional;

public interface PageScrapeRepository {
    PageScrape savePageScrape(PageScrape pageScrape);

    Optional<PageScrape> retrieveLatestScrapeWithGivenURLAndDateUpdated(String url, Instant updatedTime);
}
