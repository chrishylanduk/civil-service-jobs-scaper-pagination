package com.chrishyland.wholewebsitescraper.domain.interfaces;

import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;

import java.util.List;

public interface SitemapEntryRepository {
    void storeSitemapEntries(List<SitemapEntry> sitemapEntries);

    void replaceLatestSitemapEntryWithSameUrl(SitemapEntry replacementSitemapEntry);
}
