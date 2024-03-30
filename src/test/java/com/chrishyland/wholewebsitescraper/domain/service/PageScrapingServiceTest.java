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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PageScrapingServiceTest {

    private final SitemapEntryFetch mockSitemapEntryFetch = Mockito.mock(SitemapEntryFetch.class);
    private final PageScrapeRepository mockPageScrapeRepository = Mockito.mock(PageScrapeRepository.class);
    private final URLScraper mockURLScraper = Mockito.mock(URLScraper.class);
    private final SitemapEntryRepository mockSitemapEntryRepository = Mockito.mock(SitemapEntryRepository.class);
    private final PageScrapingService pageScrapingService = new PageScrapingService(mockSitemapEntryFetch, mockSitemapEntryRepository, mockPageScrapeRepository, mockURLScraper);


    @Test
    void findsExistingScrapeWithSameUpdatedTimeIfPresent() {
        LocalDateTime decemberTwelfth2022 = LocalDateTime.of(2022, 12, 12, 12, 12);
        SitemapEntry sitemapEntry = SitemapEntry.builder().url("https://www.example.com").updatedTime(decemberTwelfth2022).build();
        PageScrape pageScrape = PageScrape.builder().url("https://www.example.com").updatedTime(decemberTwelfth2022).build();
        Mockito.when(mockPageScrapeRepository.retrieveLatestScrapeForUrl("https://www.example.com")).thenReturn(Optional.of(pageScrape));

        Boolean existingScrapePresent = pageScrapingService.getExistingPageScrapeMatchingSitemapEntryUrlAndUpdatedTime(sitemapEntry, mockPageScrapeRepository).isPresent();

        assertEquals(true, existingScrapePresent);

    }

    @Test
    void doesNotFindExistingScrapeWithSameUpdatedTimeIfNotPresent() {
        LocalDateTime decemberTwelfth2022 = LocalDateTime.of(2022, 12, 12, 12, 12);
        LocalDateTime decemberEleventh2022 = LocalDateTime.of(2022, 12, 11, 12, 12);
        SitemapEntry sitemapEntry = SitemapEntry.builder().url("https://www.example.com").updatedTime(decemberTwelfth2022).build();
        PageScrape pageScrape = PageScrape.builder().url("https://www.example.com").updatedTime(decemberEleventh2022).build();
        Mockito.when(mockPageScrapeRepository.retrieveLatestScrapeForUrl("https://www.example.com")).thenReturn(Optional.of(pageScrape));

        Boolean existingScrapePresent = pageScrapingService.getExistingPageScrapeMatchingSitemapEntryUrlAndUpdatedTime(sitemapEntry, mockPageScrapeRepository).isPresent();

        assertEquals(false, existingScrapePresent);
    }

}