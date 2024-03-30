package com.chrishyland.csjobsdataset.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sitemap_entries")
public class SitemapEntryPO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    @SequenceGenerator(name = "id_generator", sequenceName = "sitemap_entries_seq", allocationSize = 50)
    private Long id;
    private int jcode;
    private LocalDateTime updatedTime;
    private LocalDateTime checkedTime;
    private int scrapeid;
    private Boolean scrape_is_new;
}
