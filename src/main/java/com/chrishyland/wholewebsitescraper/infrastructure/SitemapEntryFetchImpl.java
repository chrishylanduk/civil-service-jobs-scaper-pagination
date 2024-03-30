package com.chrishyland.wholewebsitescraper.infrastructure;

import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryFetch;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeParseException;
@Slf4j
public class SitemapEntryFetchImpl implements SitemapEntryFetch {

    @Override
    public List<SitemapEntry> listAllSitemapEntries(String sitemapUrl) throws IOException {

        List<SitemapEntry> sitemapEntryList = new ArrayList<>();

        Document doc = Jsoup.connect(sitemapUrl).get();
        Elements pages = doc.getElementsByTag("url");

        Instant currentTime = Instant.now();

        log.info("Found {} elements in the sitemap", pages.size());

        for (Element page : pages) {
            Element loc = page.getElementsByTag("loc").first();
            Element lastMod = page.getElementsByTag("lastmod").first();

            if (loc != null) {
                String url = loc.text();
                Instant updatedTime = null;

                if (lastMod != null) {
                    updatedTime = parseUpdatedTime(lastMod);
                }

                if (updatedTime != null) {
                    sitemapEntryList.add(SitemapEntry.builder().url(url).updatedTime(updatedTime).checkedTime(currentTime).build());
                } else {
                    sitemapEntryList.add(SitemapEntry.builder().url(url).checkedTime(currentTime).build());
                }
            }
        }
        return sitemapEntryList;
    }

    private static Instant parseUpdatedTime(Element lastmod) {
        DateTimeFormatter formatterWithOffset = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter formatterWithoutOffset = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        Instant updatedTime;
        try {
            updatedTime = OffsetDateTime.parse(lastmod.text(), formatterWithOffset).toInstant();
        } catch (DateTimeParseException e) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(lastmod.text(), formatterWithoutOffset);
                updatedTime = dateTime.atOffset(ZoneOffset.UTC).toInstant();
            }
            catch (DateTimeParseException e2) {
                updatedTime = null;
            }
        }
        return updatedTime;
    }
}
