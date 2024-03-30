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
    public List<SitemapEntry> storeSitemapEntries(List<SitemapEntry> sitemapEntries) {
        List<SitemapEntryPO> sitemapEntriesPO = sitemapEntries.stream()
                .map(this::sitemapEntryToSitemapEntryPO)
                .collect(Collectors.toList());
        List<SitemapEntryPO> savedSitemapEntryPOs = repository.saveAll(sitemapEntriesPO);

        return savedSitemapEntryPOs.stream()
                .map(this::sitemapEntryPOToSitemapEntry)
                .collect(Collectors.toList());
    }

    @Override
    public SitemapEntry saveSitemapEntry(SitemapEntry sitemapEntry) {
        SitemapEntryPO sitemapEntryPO = sitemapEntryToSitemapEntryPO(sitemapEntry);

        SitemapEntryPO savedSitemapEntryPO = repository.save(sitemapEntryPO);
        return sitemapEntryPOToSitemapEntry(savedSitemapEntryPO);
    }

    public SitemapEntryPO sitemapEntryToSitemapEntryPO(SitemapEntry sitemapEntry) {
        return SitemapEntryPO.builder()
                .sitemapEntryId(sitemapEntry.getSitemapEntryId())
                .url(sitemapEntry.getUrl())
                .updatedTime(sitemapEntry.getUpdatedTime())
                .checkedTime(sitemapEntry.getCheckedTime())
                .scrapeId(sitemapEntry.getScrapeId())
                .scrapeIsNew(sitemapEntry.getScrapeIsNew())
                .build();
    }

    public SitemapEntry sitemapEntryPOToSitemapEntry(SitemapEntryPO sitemapEntryPO) {
        return SitemapEntry.builder()
                .sitemapEntryId(sitemapEntryPO.getSitemapEntryId())
                .url(sitemapEntryPO.getUrl())
                .updatedTime(sitemapEntryPO.getUpdatedTime())
                .checkedTime(sitemapEntryPO.getCheckedTime())
                .scrapeId(sitemapEntryPO.getScrapeId())
                .scrapeIsNew(sitemapEntryPO.getScrapeIsNew())
                .build();
    }
}
