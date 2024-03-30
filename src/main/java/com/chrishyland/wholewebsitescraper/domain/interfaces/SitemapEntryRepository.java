package com.chrishyland.wholewebsitescraper.domain.interfaces;

import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;

import java.util.List;

public interface SitemapEntryRepository {
    List<SitemapEntry> storeSitemapEntries(List<SitemapEntry> sitemapEntries);

    SitemapEntry saveSitemapEntry(SitemapEntry sitemapEntry);
}
