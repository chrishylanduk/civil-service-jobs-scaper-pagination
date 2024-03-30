package com.chrishyland.wholewebsitescraper.service;

import com.chrishyland.wholewebsitescraper.domain.service.PageScrapingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.chrishyland.wholewebsitescraper.infrastructure")
@EntityScan("com.chrishyland.wholewebsitescraper.infrastructure")
@Slf4j
public class WholeWebsiteScraperCommandLineRunner implements CommandLineRunner {

    private final PageScrapingService pageScrapingService;

    public WholeWebsiteScraperCommandLineRunner(PageScrapingService pageScrapingService) {
        this.pageScrapingService = pageScrapingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WholeWebsiteScraperCommandLineRunner.class, args);
    }

    @Override
    public void run(String... args) {
        final int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                pageScrapingService.scrapePagesInSitemap(System.getenv("SITEMAP_URL"));
                log.info("Scraping successful on attempt {}", attempt);
                break;
            } catch (Exception e) {
                log.error("Failed to scrape pages from sitemap at attempt {}", attempt);
                if (attempt < maxRetries) {
                    log.info("Waiting 2 minutes before retrying");
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException ie) {
                        System.out.println("Interrupted during wait before retry" + ie);
                        break;
                    }
                } else {
                    log.error("Max retries reached. Aborting scraping.");
                }
            }
        }
    }

}
