package com.chrishyland.wholewebsitescraper.service.configuration;

import com.chrishyland.wholewebsitescraper.domain.interfaces.PageScrapeRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryFetch;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.URLScraper;
import com.chrishyland.wholewebsitescraper.domain.service.PageScrapingService;
import com.chrishyland.wholewebsitescraper.infrastructure.PageScrapeRepositoryImpl;
import com.chrishyland.wholewebsitescraper.infrastructure.SitemapEntryFetchImpl;
import com.chrishyland.wholewebsitescraper.infrastructure.SitemapEntryRepositoryImpl;
import com.chrishyland.wholewebsitescraper.infrastructure.URLScraperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public PageScrapingService pageScrapingService(SitemapEntryFetch sitemapEntryFetch,
                                                   PageScrapeRepository pageScrapeRepository,
                                                   URLScraper urlScraper, SitemapEntryRepository sitemapEntryRepository) {
        return new PageScrapingService(sitemapEntryFetch, sitemapEntryRepository, pageScrapeRepository, urlScraper);
    }

    @Bean
    public URLScraper urlScraper() {
        return new URLScraperImpl();
    }

    @Bean
    SitemapEntryFetch sitemapEntryFetch() {
        return new SitemapEntryFetchImpl();
    }

    @Bean
    SitemapEntryRepository sitemapEntryRepository() {
        return new SitemapEntryRepositoryImpl();
    }

    @Bean
    PageScrapeRepository pageScrapeRepository() {
        return new PageScrapeRepositoryImpl();
    }
}
