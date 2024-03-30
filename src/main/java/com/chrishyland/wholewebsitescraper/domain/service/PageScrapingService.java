package com.chrishyland.wholewebsitescraper.domain.service;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;
import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;
import com.chrishyland.wholewebsitescraper.domain.interfaces.PageScrapeRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryFetch;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.URLScraper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PageScrapingService {

    private SitemapEntryFetch sitemapEntryFetch;
    private SitemapEntryRepository sitemapEntryRepository;
    private PageScrapeRepository pageScrapeRepository;
    private URLScraper urlScraper;

    public void scrapePagesInSitemap(String sitemapURL) throws IOException {
        System.out.println("Started scrape pages");
        List<SitemapEntry> currentSitemapEntries = sitemapEntryFetch.listAllSitemapEntries(sitemapURL);

        List<SitemapEntry> savedSitemapEntries = sitemapEntryRepository.storeSitemapEntries(currentSitemapEntries);

        scrapePagesIfNotAlreadySavedAndUpdateSitemapEntries(savedSitemapEntries);
    }

    public void scrapePagesIfNotAlreadySavedAndUpdateSitemapEntries(List<SitemapEntry> sitemapEntries) {
        for (SitemapEntry sitemapEntry : sitemapEntries) {
            System.out.println("Starting scrape of " + sitemapEntry.getUrl());
            Optional<PageScrape> existingPageScrape = pageScrapeRepository.retrieveLatestScrapeWithGivenURLAndDateUpdated(sitemapEntry.getUrl(), sitemapEntry.getUpdatedTime());

            if (existingPageScrape.isPresent()) {
                System.out.println("Already have this scrape");
                sitemapEntryRepository.saveSitemapEntry(sitemapEntry.toBuilder()
                        .scrapeIsNew(false)
                        .scrapeId(existingPageScrape.get().getScrapeId())
                        .build());
            } else {
                try {
                    PageScrape savedPageScrape = scrapeAndSavePageFromSitemapEntry(sitemapEntry);
                    sitemapEntryRepository.saveSitemapEntry(sitemapEntry.toBuilder()
                            .scrapeIsNew(true)
                            .scrapeId(savedPageScrape.getScrapeId())
                            .build());
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private PageScrape scrapeAndSavePageFromSitemapEntry(SitemapEntry sitemapEntry) throws IOException, InterruptedException {
        String html = urlScraper.getHtmlOfUrl(sitemapEntry.getUrl());

        PageScrape pageScrape = PageScrape.builder()
                .url(sitemapEntry.getUrl())
                .scrapedTime(Instant.now())
                .html(html)
                .updatedTime(sitemapEntry.getUpdatedTime())
                .build();

        return pageScrapeRepository.savePageScrape(pageScrape);
    }
}
