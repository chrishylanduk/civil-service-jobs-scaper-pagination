package com.chrishyland.wholewebsitescraper.infrastructure;

import com.chrishyland.wholewebsitescraper.domain.entity.PageScrape;
import com.chrishyland.wholewebsitescraper.domain.interfaces.PageScrapeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PageScrapeRepositoryImpl implements PageScrapeRepository {

    @Autowired
    private PageScrapePOJPARepository repository;

    @Override
    public void storePageScrape(PageScrape pageScrape) {
        PageScrapePO pageScrapePO = pageScrapeToPageScrapePO(pageScrape);
        repository.save(pageScrapePO);
    }

    @Override
    public Optional<PageScrape> retrieveLatestScrapeForUrl(String url) {
        PageScrapePO pageScrapePO = repository.findTopByUrlOrderByUpdatedTimeDesc(url);
        if (pageScrapePO == null) {
            return Optional.empty();
        } else {
            return Optional.of(pageScrapePOToPageScrape(pageScrapePO));
        }

    }

    public PageScrapePO pageScrapeToPageScrapePO(PageScrape pageScrape) {
        return PageScrapePO.builder()
                .scrapedTime(pageScrape.getScrapedTime())
                .updatedTime(pageScrape.getUpdatedTime())
                .html(pageScrape.getHtml())
                .url(pageScrape.getUrl())
                .build();
    }

    public PageScrape pageScrapePOToPageScrape(PageScrapePO pageScrapePo) {
        return PageScrape.builder()
                .scrapedTime(pageScrapePo.getScrapedTime())
                .updatedTime(pageScrapePo.getUpdatedTime())
                .html(pageScrapePo.getHtml())
                .url(pageScrapePo.getUrl())
                .build();
    }
}
