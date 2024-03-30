package com.chrishyland.csjobsdataset.domain.interfaces;

import com.chrishyland.csjobsdataset.domain.entity.SitemapEntry;

import java.util.List;

public interface SitemapEntryRepository {
    void storeSitemapEntries(List<SitemapEntry> sitemapEntries);

    void replaceLatestSitemapEntryWithSameJcode(SitemapEntry replacementSitemapEntry);
}
