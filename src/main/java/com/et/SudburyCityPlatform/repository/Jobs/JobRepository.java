package com.et.SudburyCityPlatform.repository.Jobs;

import com.et.SudburyCityPlatform.models.jobs.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
