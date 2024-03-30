package com.chrishyland.wholewebsitescraper.infrastructure;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;
import com.chrishyland.wholewebsitescraper.domain.interfaces.PageScrapeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;

public class PageScrapeRepositoryImpl implements PageScrapeRepository {

    @Autowired
    private PageScrapePOJPARepository repository;

    @Override
    public PageScrape savePageScrape(PageScrape pageScrape) {
        PageScrapePO pageScrapePO = pageScrapeToPageScrapePO(pageScrape);
        PageScrapePO savedPageScrapePO = repository.save(pageScrapePO);
        return pageScrapePOToPageScrape(savedPageScrapePO);
    }

    @Override
    public Optional<PageScrape> retrieveLatestScrapeWithSpecificURLAndDateUpdated(String url, Instant updatedTime) {
        if (updatedTime == null || url == null) {
            return Optional.empty();
        }

        PageScrapePO pageScrapePO = repository.findTopByUrlAndUpdatedTimeOrderByScrapeIdDesc(url, updatedTime);
        if (pageScrapePO == null) {
            return Optional.empty();
        } else {
            return Optional.of(pageScrapePOToPageScrape(pageScrapePO));
        }
    }

    public PageScrapePO pageScrapeToPageScrapePO(PageScrape pageScrape) {
        return PageScrapePO.builder()
                .scrapeId(pageScrape.getScrapeId())
                .scrapedTime(pageScrape.getScrapedTime())
                .updatedTime(pageScrape.getUpdatedTime())
                .html(pageScrape.getHtml())
                .url(pageScrape.getUrl())
                .build();
    }

    public PageScrape pageScrapePOToPageScrape(PageScrapePO pageScrapePo) {
        return PageScrape.builder()
                .scrapeId(pageScrapePo.getScrapeId())
                .scrapedTime(pageScrapePo.getScrapedTime())
                .updatedTime(pageScrapePo.getUpdatedTime())
                .html(pageScrapePo.getHtml())
                .url(pageScrapePo.getUrl())
                .build();
    }
}
