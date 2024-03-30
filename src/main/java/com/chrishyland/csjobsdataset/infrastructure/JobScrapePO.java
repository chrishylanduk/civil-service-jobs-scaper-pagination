package com.chrishyland.csjobsdataset.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scrapes")
public class JobScrapePO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapeid;
    private int jcode;
    private String html;
    private LocalDateTime updatedTime;
    private LocalDateTime scrapedTime;
}
