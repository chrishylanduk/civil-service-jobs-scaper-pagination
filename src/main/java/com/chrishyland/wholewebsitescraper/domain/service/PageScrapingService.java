package com.chrishyland.wholewebsitescraper.domain.service;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;
import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;
import com.chrishyland.wholewebsitescraper.domain.interfaces.PageScrapeRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryFetch;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.URLScraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class PageScrapingService {

    private SitemapEntryFetch sitemapEntryFetch;
    private SitemapEntryRepository sitemapEntryRepository;
    private PageScrapeRepository pageScrapeRepository;
    private URLScraper urlScraper;

    public void scrapePagesInSitemap(String sitemapURL) throws IOException {
        log.info("Started scrape pages");
        List<SitemapEntry> currentSitemapEntries = sitemapEntryFetch.listAllSitemapEntries(sitemapURL);

        List<SitemapEntry> savedSitemapEntries = sitemapEntryRepository.storeSitemapEntries(currentSitemapEntries);

        scrapePagesIfNotAlreadySavedAndUpdateSitemapEntries(savedSitemapEntries);
    }

    public void scrapePagesIfNotAlreadySavedAndUpdateSitemapEntries(List<SitemapEntry> sitemapEntries) {
        for (SitemapEntry sitemapEntry : sitemapEntries) {
            log.info("Starting scrape of {}", sitemapEntry.getUrl());
            Optional<PageScrape> existingPageScrape = pageScrapeRepository.retrieveLatestScrapeWithGivenURLAndDateUpdated(sitemapEntry.getUrl(), sitemapEntry.getUpdatedTime());

            if (existingPageScrape.isPresent()) {
                log.info("Already have this scrape");
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
                } catch (IOException | ParseException e) {
                    log.error("Error getting HTML of url", e);
                }
            }
        }
    }

    private PageScrape scrapeAndSavePageFromSitemapEntry(SitemapEntry sitemapEntry) throws IOException, ParseException {
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
