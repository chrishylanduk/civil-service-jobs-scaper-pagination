package com.chrishyland.wholewebsitescraper.infrastructure;

import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryFetch;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@Slf4j
public class SitemapEntryFetchImpl implements SitemapEntryFetch {

    @Override
    public List<SitemapEntry> listAllSitemapEntries(String startUrl) throws IOException {

        List<SitemapEntry> sitemapEntryList = new ArrayList<>();
        Instant currentTime = Instant.now();

        String currentPageUrl = startUrl;
        int currentPageNumber = 0;
        boolean hasNextPage = true;

        while (hasNextPage) {
            currentPageNumber += 1;
            Document pageDoc = Jsoup.connect(currentPageUrl).get();

            // Extract job links from the current page
            Elements jobTitles = pageDoc.select("h3.search-results-job-box-title");
            for (Element jobTitle : jobTitles) {
                Element jobLink = jobTitle.selectFirst("a");
                if (jobLink != null) {
                    String pageUrl = jobLink.attr("href");

                    // Extract the SID parameter and decode it
                    String sid = null;
                    try {
                        List<NameValuePair> params = URLEncodedUtils.parse(new URI(pageUrl), StandardCharsets.UTF_8);
                        for (NameValuePair param : params) {
                            if (param.getName().equals("SID")) {
                                sid = param.getValue();
                                break;
                            }
                        }
                    } catch (URISyntaxException e) {
                        continue;
                    }

                    if (sid != null) {
                        // Base64 decode the SID
                        byte[] decodedBytes = Base64.getDecoder().decode(sid);
                        String decodedSid = new String(decodedBytes, StandardCharsets.UTF_8);

                        String jcode = null;
                        try {
                            List<NameValuePair> params = URLEncodedUtils.parse(new URI("https://www.example.com/?" + decodedSid), StandardCharsets.UTF_8);
                            for (NameValuePair param : params) {
                                if (param.getName().equals("joblist_view_vac")) {
                                    jcode = param.getValue();
                                    break;
                                }
                            }
                        } catch (URISyntaxException e) {
                            continue;
                        }

                        if (jcode != null) {
                            String finalJobUrl = "https://www.civilservicejobs.service.gov.uk/csr/jobs.cgi?jcode=" + jcode;
                            log.info("Found job with URL: {}", finalJobUrl);
                            sitemapEntryList.add(SitemapEntry.builder()
                                    .url(finalJobUrl)
                                    .checkedTime(currentTime)
                                    .build());
                        }

                    }
                }
            }

            // Check if there's a next page
            Element nextPageLink = pageDoc.selectFirst("a:contains(next »)");
            if (nextPageLink != null) {
                currentPageUrl = nextPageLink.attr("href");
            } else {
                hasNextPage = false;
            }
            log.info("Processed job search page number {}: {}", currentPageNumber, currentPageUrl);
        }

        log.info("Found {} job entries",  sitemapEntryList.size());
        return sitemapEntryList;
    }

}
