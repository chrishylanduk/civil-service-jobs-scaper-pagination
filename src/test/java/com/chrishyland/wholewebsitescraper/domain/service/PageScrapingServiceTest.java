package com.chrishyland.wholewebsitescraper.domain.service;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;
import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;
import com.chrishyland.wholewebsitescraper.domain.interfaces.PageScrapeRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryFetch;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryRepository;
import com.chrishyland.wholewebsitescraper.domain.interfaces.URLScraper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PageScrapingServiceTest {

    private final SitemapEntryFetch mockSitemapEntryFetch = Mockito.mock(SitemapEntryFetch.class);
    private final PageScrapeRepository mockPageScrapeRepository = Mockito.mock(PageScrapeRepository.class);
    private final URLScraper mockURLScraper = Mockito.mock(URLScraper.class);
    private final SitemapEntryRepository mockSitemapEntryRepository = Mockito.mock(SitemapEntryRepository.class);
    private final PageScrapingService pageScrapingService = new PageScrapingService(mockSitemapEntryFetch, mockSitemapEntryRepository, mockPageScrapeRepository, mockURLScraper);


    @Test
    void doesNotFetchPageHtmlIfExistingScrapeWithSameUpdatedTimePresent() {
        Instant decemberTwelfth2022 = ZonedDateTime.of(2022, 12, 12, 12, 12, 0, 0, ZoneId.of("UTC")).toInstant();
        SitemapEntry sitemapEntry = SitemapEntry.builder().url("https://www.example.com").updatedTime(decemberTwelfth2022).build();
        PageScrape pageScrape = PageScrape.builder().url("https://www.example.com").updatedTime(decemberTwelfth2022).build();
        Mockito.when(mockPageScrapeRepository.retrieveLatestScrapeWithGivenURLAndDateUpdated("https://www.example.com", decemberTwelfth2022)).thenReturn(Optional.of(pageScrape));

        pageScrapingService.scrapePagesIfNotAlreadySavedAndUpdateSitemapEntries(List.of(sitemapEntry));

        try {
            Mockito.verify(mockURLScraper, Mockito.never()).getHtmlOfUrl(Mockito.anyString());
        } catch (IOException | InterruptedException e) {

        }
    }

    @Test
    void doesFetchPageHtmlIfExistingScrapeWithSameUpdatedTimeNotPresent() {
        Instant decemberTwelfth2022 = ZonedDateTime.of(2022, 12, 12, 12, 12, 0, 0, ZoneId.of("UTC")).toInstant();
        SitemapEntry sitemapEntry = SitemapEntry.builder().url("https://www.example.com").updatedTime(decemberTwelfth2022).build();
        PageScrape pageScrape = PageScrape.builder().url("https://www.example.com").updatedTime(decemberTwelfth2022).build();
        Mockito.when(mockPageScrapeRepository.retrieveLatestScrapeWithGivenURLAndDateUpdated("https://www.example.com", decemberTwelfth2022)).thenReturn(Optional.empty());
        Mockito.when(mockPageScrapeRepository.savePageScrape(Mockito.any(PageScrape.class))).thenReturn(pageScrape);

        pageScrapingService.scrapePagesIfNotAlreadySavedAndUpdateSitemapEntries(List.of(sitemapEntry));

        try {
            Mockito.verify(mockURLScraper).getHtmlOfUrl(Mockito.anyString());
        } catch (IOException | InterruptedException e) {

        }
    }
}