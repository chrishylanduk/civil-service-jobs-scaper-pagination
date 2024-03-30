package com.chrishyland.csjobsdataset.domain.service;

import com.chrishyland.csjobsdataset.domain.entity.JobScrape;
import com.chrishyland.csjobsdataset.domain.entity.SitemapEntry;
import com.chrishyland.csjobsdataset.domain.interfaces.JobScrapeRepository;
import com.chrishyland.csjobsdataset.domain.interfaces.SitemapEntryFetch;
import com.chrishyland.csjobsdataset.domain.interfaces.SitemapEntryRepository;
import com.chrishyland.csjobsdataset.domain.interfaces.URLScraper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class JobScrapingService {

    private SitemapEntryFetch sitemapEntryFetch;
    private SitemapEntryRepository sitemapEntryRepository;
    private JobScrapeRepository jobScrapeRepository;
    private URLScraper urlScraper;

    public void scrapeJobs(String sitemapURL, String siteRootURL) throws IOException {
        System.out.println("Started scrape jobs");
        List<SitemapEntry> currentSitemapEntries = sitemapEntryFetch.listAllSitemapEntries(sitemapURL);

        sitemapEntryRepository.storeSitemapEntries(currentSitemapEntries);

        for (SitemapEntry sitemapEntry : currentSitemapEntries) {
            Optional<JobScrape> existingJobScrape = getExistingJobScrapeMatchingSitemapEntryJcodeAndUpdatedTime(sitemapEntry, jobScrapeRepository);

            if (existingJobScrape.isPresent()) {
                System.out.println("Already have this scrape");
                sitemapEntryRepository.replaceLatestSitemapEntryWithSameJcode(sitemapEntry.toBuilder()
                        .scrape_is_new(false)
                        .scrapeid(existingJobScrape.get().getJcode())
                        .build());
            } else {
                try {
                    String html = urlScraper.getHtmlOfUrl(
                            siteRootURL + sitemapEntry.getJcode());

                    JobScrape jobScrape = JobScrape.builder()
                            .jcode(sitemapEntry.getJcode())
                            .scrapedTime(LocalDateTime.now())
                            .html(html)
                            .updatedTime(sitemapEntry.getUpdatedTime())
                            .build();
                    jobScrapeRepository.storeJobScrape(jobScrape);
                    sitemapEntryRepository.replaceLatestSitemapEntryWithSameJcode(sitemapEntry.toBuilder()
                            .scrape_is_new(true)
                            .scrapeid(jobScrape.getJcode())
                            .build());
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Optional<JobScrape> getExistingJobScrapeMatchingSitemapEntryJcodeAndUpdatedTime(SitemapEntry sitemapEntry, JobScrapeRepository jobScrapeRepository) {
        //TODO: This method adapted from another, should now amend the db query so isn't necessarily 'latest'
        Optional<JobScrape> latestJobScrapeOptional = jobScrapeRepository.retrieveLatestJobScrape(sitemapEntry.getJcode());

        if (latestJobScrapeOptional.isPresent() && latestJobScrapeOptional.get().getUpdatedTime().isEqual(sitemapEntry.getUpdatedTime())) {
            return latestJobScrapeOptional;
        } else {
            return Optional.empty();
        }
    }
}
