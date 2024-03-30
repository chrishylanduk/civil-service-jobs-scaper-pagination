package com.chrishyland.csjobsdataset.domain.interfaces;

import com.chrishyland.csjobsdataset.domain.entity.JobScrape;

import java.util.Optional;

public interface JobScrapeRepository {
    void storeJobScrape(JobScrape jobScrape);

    Optional<JobScrape> retrieveLatestJobScrape(int jcode);
}
