package com.chrishyland.csjobsdataset.domain.service;

import com.chrishyland.csjobsdataset.domain.entity.JobScrape;
import com.chrishyland.csjobsdataset.domain.entity.SitemapEntry;
import com.chrishyland.csjobsdataset.domain.interfaces.JobScrapeRepository;
import com.chrishyland.csjobsdataset.domain.interfaces.SitemapEntryFetch;
import com.chrishyland.csjobsdataset.domain.interfaces.SitemapEntryRepository;
import com.chrishyland.csjobsdataset.domain.interfaces.URLScraper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class JobScrapingServiceTest {

    private final SitemapEntryFetch mockSitemapEntryFetch = Mockito.mock(SitemapEntryFetch.class);
    private final JobScrapeRepository mockJobScrapeRepository = Mockito.mock(JobScrapeRepository.class);
    private final URLScraper mockURLScraper = Mockito.mock(URLScraper.class);
    private final SitemapEntryRepository mockSitemapEntryRepository = Mockito.mock(SitemapEntryRepository.class);
    private final JobScrapingService jobScrapingService = new JobScrapingService(mockSitemapEntryFetch, mockSitemapEntryRepository, mockJobScrapeRepository, mockURLScraper);


    @Test
    void findsExistingScrapeWithSameUpdatedTimeIfPresent() {
        LocalDateTime decemberTwelfth2022 = LocalDateTime.of(2022, 12, 12, 12, 12);
        SitemapEntry sitemapEntry = SitemapEntry.builder().jcode(1234).updatedTime(decemberTwelfth2022).build();
        JobScrape jobScrape = JobScrape.builder().jcode(1234).updatedTime(decemberTwelfth2022).build();
        Mockito.when(mockJobScrapeRepository.retrieveLatestJobScrape(1234)).thenReturn(Optional.of(jobScrape));

        Boolean existingScrapePresent = jobScrapingService.getExistingJobScrapeMatchingSitemapEntryJcodeAndUpdatedTime(sitemapEntry, mockJobScrapeRepository).isPresent();

        assertEquals(true, existingScrapePresent);

    }

    @Test
    void doesNotFindExistingScrapeWithSameUpdatedTimeIfNotPresent() {
        LocalDateTime decemberTwelfth2022 = LocalDateTime.of(2022, 12, 12, 12, 12);
        LocalDateTime decemberEleventh2022 = LocalDateTime.of(2022, 12, 11, 12, 12);
        SitemapEntry sitemapEntry = SitemapEntry.builder().jcode(1234).updatedTime(decemberTwelfth2022).build();
        JobScrape jobScrape = JobScrape.builder().jcode(1234).updatedTime(decemberEleventh2022).build();
        Mockito.when(mockJobScrapeRepository.retrieveLatestJobScrape(1234)).thenReturn(Optional.of(jobScrape));

        Boolean existingScrapePresent = jobScrapingService.getExistingJobScrapeMatchingSitemapEntryJcodeAndUpdatedTime(sitemapEntry, mockJobScrapeRepository).isPresent();

        assertEquals(false, existingScrapePresent);
    }

}