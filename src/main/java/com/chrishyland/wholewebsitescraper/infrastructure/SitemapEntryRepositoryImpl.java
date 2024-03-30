package com.chrishyland.wholewebsitescraper.infrastructure;

import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class SitemapEntryRepositoryImpl implements SitemapEntryRepository {

    @Autowired
    private SitemapEntryPOJPARepository repository;

    @Override
    public void storeSitemapEntries(List<SitemapEntry> sitemapEntries) {
        List<SitemapEntryPO> sitemapEntriesPO = sitemapEntries.stream()
                .map(this::sitemapEntryToSitemapEntryPO)
                .collect(Collectors.toList());
        repository.saveAll(sitemapEntriesPO);
    }

    @Override
    public void replaceLatestSitemapEntryWithSameUrl(SitemapEntry replacementSitemapEntry) {
        SitemapEntryPO replacementSitemapEntryPO = sitemapEntryToSitemapEntryPO(replacementSitemapEntry);

        SitemapEntryPO existingEntryPO = repository.findTopByUrlOrderByIdDesc(replacementSitemapEntryPO.getUrl());
        replacementSitemapEntryPO = replacementSitemapEntryPO.toBuilder().id(existingEntryPO.getId()).build();

        repository.save(replacementSitemapEntryPO);
    }

    public SitemapEntryPO sitemapEntryToSitemapEntryPO(SitemapEntry sitemapEntry) {
        return SitemapEntryPO.builder()
                .url(sitemapEntry.getUrl())
                .updatedTime(sitemapEntry.getUpdatedTime())
                .checkedTime(sitemapEntry.getCheckedTime())
                .scrape_id(sitemapEntry.getScrape_id())
                .scrape_is_new(sitemapEntry.getScrape_is_new())
                .build();
    }
}
