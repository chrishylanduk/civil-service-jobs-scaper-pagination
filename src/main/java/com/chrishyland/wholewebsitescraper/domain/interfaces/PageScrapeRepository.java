package com.chrishyland.wholewebsitescraper.domain.interfaces;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;

import java.util.Optional;

public interface PageScrapeRepository {
    void storePageScrape(PageScrape pageScrape);

    Optional<PageScrape> retrieveLatestScrapeForUrl(String url);
}
