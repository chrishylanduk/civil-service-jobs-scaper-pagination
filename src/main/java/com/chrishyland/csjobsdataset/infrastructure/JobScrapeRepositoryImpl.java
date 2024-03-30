package com.chrishyland.csjobsdataset.infrastructure;

import com.chrishyland.csjobsdataset.domain.entity.JobScrape;
import com.chrishyland.csjobsdataset.domain.interfaces.JobScrapeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class JobScrapeRepositoryImpl implements JobScrapeRepository {

    @Autowired
    private JobScrapePOJPARepository repository;

    @Override
    public void storeJobScrape(JobScrape jobScrape) {
        JobScrapePO jobScrapePO = jobScrapeToJobScrapePO(jobScrape);
        repository.save(jobScrapePO);
    }

    @Override
    public Optional<JobScrape> retrieveLatestJobScrape(int jcode) {
        JobScrapePO jobScrapePO = repository.findTopByJcodeOrderByUpdatedTimeDesc(jcode);
        if (jobScrapePO == null) {
            return Optional.empty();
        } else {
            return Optional.of(jobScrapePOToJobScrape(jobScrapePO));
        }

    }

    public JobScrapePO jobScrapeToJobScrapePO(JobScrape jobScrape) {
        return JobScrapePO.builder()
                .scrapedTime(jobScrape.getScrapedTime())
                .updatedTime(jobScrape.getUpdatedTime())
                .html(jobScrape.getHtml())
                .jcode(jobScrape.getJcode())
                .build();
    }

    public JobScrape jobScrapePOToJobScrape(JobScrapePO jobScrapePo) {
        return JobScrape.builder()
                .scrapedTime(jobScrapePo.getScrapedTime())
                .updatedTime(jobScrapePo.getUpdatedTime())
                .html(jobScrapePo.getHtml())
                .jcode(jobScrapePo.getJcode())
                .build();
    }
}
