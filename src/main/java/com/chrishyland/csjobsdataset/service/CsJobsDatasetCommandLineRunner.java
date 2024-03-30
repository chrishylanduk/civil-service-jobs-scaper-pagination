package com.chrishyland.csjobsdataset.service;

import com.chrishyland.csjobsdataset.domain.service.JobScrapingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.chrishyland.csjobsdataset.infrastructure")
@EntityScan("com.chrishyland.csjobsdataset.infrastructure")
public class CsJobsDatasetCommandLineRunner implements CommandLineRunner {

    private final JobScrapingService jobScrapingService;

    public CsJobsDatasetCommandLineRunner(JobScrapingService jobScrapingService) {
        this.jobScrapingService = jobScrapingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(CsJobsDatasetCommandLineRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        jobScrapingService.scrapeJobs(System.getenv("SITEMAP_URL"), System.getenv("SITE_ROOT_URL"));

    }

}
