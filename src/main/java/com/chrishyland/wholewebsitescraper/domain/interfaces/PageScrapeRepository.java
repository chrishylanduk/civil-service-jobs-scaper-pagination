package com.chrishyland.wholewebsitescraper.domain.interfaces;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;

import java.util.Optional;

public interface PageScrapeRepository {
    PageScrape savePageScrape(PageScrape pageScrape);

    Optional<PageScrape> retrieveLatestScrapeWithSpecificURL(String url);
}
