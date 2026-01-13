package com.et.SudburyCityPlatform.service.Jobs;

import com.et.SudburyCityPlatform.models.jobs.Job;
import com.et.SudburyCityPlatform.repository.Jobs.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(Job job) {
        job.setPostedDate(LocalDate.now());
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job updateJob(Job job) {
        Job desiredJob = jobRepository.findById(job.getId())
                .orElseThrow(() ->
                        new RuntimeException("Job not found with id: " + job.getId())
                );

        desiredJob.setDescription(job.getDescription());
        desiredJob.setSalary(job.getSalary());
        desiredJob.setEmploymentType(job.getEmploymentType());
        desiredJob.setPostedDate(LocalDate.now());
        desiredJob.setRequirements(job.getRequirements());

        return jobRepository.save(desiredJob);
    }
}
