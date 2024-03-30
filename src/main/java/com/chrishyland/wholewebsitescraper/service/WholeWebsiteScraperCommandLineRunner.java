package com.chrishyland.wholewebsitescraper.service;

import com.chrishyland.wholewebsitescraper.domain.service.PageScrapingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.chrishyland.wholewebsitescraper.infrastructure")
@EntityScan("com.chrishyland.wholewebsitescraper.infrastructure")
public class WholeWebsiteScraperCommandLineRunner implements CommandLineRunner {

    private final PageScrapingService pageScrapingService;

    public WholeWebsiteScraperCommandLineRunner(PageScrapingService pageScrapingService) {
        this.pageScrapingService = pageScrapingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WholeWebsiteScraperCommandLineRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        pageScrapingService.scrapePages(System.getenv("SITEMAP_URL"));
    }

}
