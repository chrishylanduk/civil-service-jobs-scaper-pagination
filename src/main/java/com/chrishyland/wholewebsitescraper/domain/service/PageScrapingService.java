package com.chrishyland.wholewebsitescraper.domain.service;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;
import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;
import com.chrishyland.wholewebsitescraper.domain.interfaces.PageScrapeRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryFetch;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.URLScraper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PageScrapingService {

    private SitemapEntryFetch sitemapEntryFetch;
    private SitemapEntryRepository sitemapEntryRepository;
    private PageScrapeRepository pageScrapeRepository;
    private URLScraper urlScraper;

    public void scrapePages(String sitemapURL) throws IOException {
        System.out.println("Started scrape pages");
        List<SitemapEntry> currentSitemapEntries = sitemapEntryFetch.listAllSitemapEntries(sitemapURL);

        sitemapEntryRepository.storeSitemapEntries(currentSitemapEntries);

        for (SitemapEntry sitemapEntry : currentSitemapEntries) {
            System.out.println("Starting scrape of " + sitemapEntry.getUrl());
            Optional<PageScrape> existingPageScrape = getExistingPageScrapeMatchingSitemapEntryUrlAndUpdatedTime(sitemapEntry, pageScrapeRepository);

            if (existingPageScrape.isPresent()) {
                System.out.println("Already have this scrape");
                sitemapEntryRepository.replaceLatestSitemapEntryWithSameUrl(sitemapEntry.toBuilder()
                        .scrape_is_new(false)
                        .build());
            } else {
                try {
                    String html = urlScraper.getHtmlOfUrl(sitemapEntry.getUrl());

                    PageScrape pageScrape = PageScrape.builder()
                            .url(sitemapEntry.getUrl())
                            .scrapedTime(LocalDateTime.now())
                            .html(html)
                            .updatedTime(sitemapEntry.getUpdatedTime())
                            .build();
                    pageScrapeRepository.storePageScrape(pageScrape);
                    sitemapEntryRepository.replaceLatestSitemapEntryWithSameUrl(sitemapEntry.toBuilder()
                            .scrape_is_new(true)
                            .build());
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Optional<PageScrape> getExistingPageScrapeMatchingSitemapEntryUrlAndUpdatedTime(SitemapEntry sitemapEntry, PageScrapeRepository pageScrapeRepository) {
        //TODO: This method adapted from another, should now amend the db query so isn't necessarily 'latest'
        Optional<PageScrape> latestPageScrapeOptional = pageScrapeRepository.retrieveLatestScrapeForUrl(sitemapEntry.getUrl());

        if (latestPageScrapeOptional.isPresent() && latestPageScrapeOptional.get().getUpdatedTime().isEqual(sitemapEntry.getUpdatedTime())) {
            return latestPageScrapeOptional;
        } else {
            return Optional.empty();
        }
    }
}
