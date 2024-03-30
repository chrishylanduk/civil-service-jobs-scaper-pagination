package com.chrishyland.csjobsdataset.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SitemapEntryPOJPARepository extends JpaRepository<SitemapEntryPO, Long> {
    SitemapEntryPO findTopByJcodeOrderByIdDesc(int jcode);
}
