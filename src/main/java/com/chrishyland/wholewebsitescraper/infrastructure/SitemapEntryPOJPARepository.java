package com.chrishyland.wholewebsitescraper.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SitemapEntryPOJPARepository extends JpaRepository<SitemapEntryPO, Long> {
    SitemapEntryPO findTopByUrlOrderBySitemapEntryIdDesc(String url);
}
