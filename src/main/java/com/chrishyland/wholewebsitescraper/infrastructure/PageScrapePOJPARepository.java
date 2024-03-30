package com.chrishyland.wholewebsitescraper.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PageScrapePOJPARepository extends JpaRepository<PageScrapePO, Long> {
    PageScrapePO findTopByUrlOrderByUpdatedTimeDesc(String url);
}