package com.et.SudburyCityPlatform.repository.Jobs;

import com.et.SudburyCityPlatform.models.jobs.JobApplicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository
        extends JpaRepository<JobApplicationRequest, Long> {
    boolean existsByJobIdAndEmail(Long jobId, String email);
}
