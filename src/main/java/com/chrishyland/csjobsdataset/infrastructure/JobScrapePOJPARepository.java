package com.chrishyland.csjobsdataset.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobScrapePOJPARepository extends JpaRepository<JobScrapePO, Long> {
    JobScrapePO findTopByJcodeOrderByUpdatedTimeDesc(int jcode);
}