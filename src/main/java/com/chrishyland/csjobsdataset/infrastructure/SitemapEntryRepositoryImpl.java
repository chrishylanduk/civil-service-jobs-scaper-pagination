package com.chrishyland.csjobsdataset.infrastructure;

import com.chrishyland.csjobsdataset.domain.entity.SitemapEntry;
import com.chrishyland.csjobsdataset.domain.interfaces.SitemapEntryRepository;
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
    public void replaceLatestSitemapEntryWithSameJcode(SitemapEntry replacementSitemapEntry) {
        SitemapEntryPO replacementSitemapEntryPO = sitemapEntryToSitemapEntryPO(replacementSitemapEntry);

        SitemapEntryPO existingEntryPO = repository.findTopByJcodeOrderByIdDesc(replacementSitemapEntryPO.getJcode());
        replacementSitemapEntryPO = replacementSitemapEntryPO.toBuilder().id(existingEntryPO.getId()).build();

        repository.save(replacementSitemapEntryPO);
    }

    public SitemapEntryPO sitemapEntryToSitemapEntryPO(SitemapEntry sitemapEntry) {
        return SitemapEntryPO.builder()
                .jcode(sitemapEntry.getJcode())
                .updatedTime(sitemapEntry.getUpdatedTime())
                .checkedTime(sitemapEntry.getCheckedTime())
                .scrapeid(sitemapEntry.getScrapeid())
                .scrape_is_new(sitemapEntry.getScrape_is_new())
                .build();
    }
}
