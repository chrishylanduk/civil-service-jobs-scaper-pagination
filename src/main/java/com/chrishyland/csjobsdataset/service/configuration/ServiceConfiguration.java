package com.chrishyland.csjobsdataset.service.configuration;

import com.chrishyland.csjobsdataset.domain.interfaces.JobScrapeRepository;
import com.chrishyland.csjobsdataset.domain.interfaces.SitemapEntryFetch;
import com.chrishyland.csjobsdataset.domain.interfaces.SitemapEntryRepository;
import com.chrishyland.csjobsdataset.domain.interfaces.URLScraper;
import com.chrishyland.csjobsdataset.domain.service.JobScrapingService;
import com.chrishyland.csjobsdataset.infrastructure.JobScrapeRepositoryImpl;
import com.chrishyland.csjobsdataset.infrastructure.SitemapEntryFetchImpl;
import com.chrishyland.csjobsdataset.infrastructure.SitemapEntryRepositoryImpl;
import com.chrishyland.csjobsdataset.infrastructure.URLScraperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public JobScrapingService jobScrapingService(SitemapEntryFetch sitemapEntryFetch,
                                                 JobScrapeRepository jobScrapeRepository,
                                                 URLScraper urlScraper, SitemapEntryRepository sitemapEntryRepository) {
        return new JobScrapingService(sitemapEntryFetch, sitemapEntryRepository, jobScrapeRepository, urlScraper);
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
    JobScrapeRepository jobScrapeRepository() {
        return new JobScrapeRepositoryImpl();
    }
}
