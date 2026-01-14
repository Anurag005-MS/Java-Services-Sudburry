package com.et.SudburyCityPlatform.service.Jobs;

import com.et.SudburyCityPlatform.models.jobs.Job;
import com.et.SudburyCityPlatform.models.jobs.JobApplicationRequest;
import com.et.SudburyCityPlatform.repository.Jobs.JobApplicationRepository;
import com.et.SudburyCityPlatform.repository.Jobs.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

   private final JobApplicationRepository applicationRepository;

    @Autowired
    public JobService(JobRepository jobRepository, JobApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;


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



    public JobApplicationRequest applyForJob(Long jobId, JobApplicationRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // 2️⃣ Prevent duplicate applications (optional but recommended)
        boolean alreadyApplied =
                applicationRepository.existsByJobIdAndEmail(jobId, request.getEmail());

        if (alreadyApplied) {
            throw new RuntimeException("You have already applied for this job");
        }

        // 3️⃣ Attach job to application
        request.setJob(job);

        // 4️⃣ Save application
        return applicationRepository.save(request);
    }
}
