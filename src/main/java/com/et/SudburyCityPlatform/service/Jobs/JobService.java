package com.et.SudburyCityPlatform.service.Jobs;

import com.et.SudburyCityPlatform.models.jobs.*;
import com.et.SudburyCityPlatform.repository.Jobs.JobApplicationRepository;
import com.et.SudburyCityPlatform.repository.Jobs.JobRepository;
import com.et.SudburyCityPlatform.repository.Jobs.JobSeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

   private final JobApplicationRepository applicationRepository;

    @Autowired
    private JobSeekerProfileRepository profileRepository;

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

        boolean alreadyApplied =
                applicationRepository.existsByJobIdAndEmail(jobId, request.getEmail());

        if (alreadyApplied) {
            throw new RuntimeException("You have already applied for this job");
        }

        JobSeekerProfile profile =
                profileRepository.findByEmail(request.getEmail()).orElse(null);

        if (profile != null) {
            request.setResumeUrl(profile.getResumeUrl());
            request.setYearsOfExperience(profile.getYearsOfExperience());

            if (request.getFirstName() == null || request.getFirstName().isBlank()) {
                request.setFirstName(profile.getFullName());
            }
        }
        request.setJob(job);
        return applicationRepository.save(request);
    }


    public List<Job> getJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployerId(employerId);
    }
    public List<JobApplicationRequest> getApplicationsForJob(Long jobId, Long employerId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return applicationRepository.findByJobId(jobId);
    }

    public List<JobApplicationRequest> getApplicationsByEmail(String email) {
        return applicationRepository.findByEmail(email);
    }


    public JobApplicationRequest updateApplicationStatus(
            Long applicationId,
            ApplicationStatus status,
            Long employerId) {

        JobApplicationRequest application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));

        Job job = application.getJob();

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("Unauthorized status update");
        }

        application.setStatus(status);

        return applicationRepository.save(application);
    }

    public JobApplicationRequest getApplicationDetails(Long applicationId, Long employerId) {

        JobApplicationRequest application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));

        Job job = application.getJob();

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("Unauthorized access to application");
        }

        return application;
    }
    public List<Job> searchJobs(String location, String type, Double minSalary) {
        return jobRepository.search(location, type, minSalary);
    }
    public List<Job> matchJobsByProfile(String email) {

        JobSeekerProfile profile =
                profileRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("Profile required"));

        return jobRepository.findAll().stream()
                .filter(job ->
                        profile.getSkills().stream().anyMatch(skill ->
                                job.getRequirements() != null &&
                                        job.getRequirements()
                                                .toLowerCase()
                                                .contains(skill.toLowerCase())
                        )
                )
                .toList();
    }

    public List<ApplicationSummary> summary(String email) {
        return applicationRepository.summary(email);
    }
    public EmployerStats stats(Long employerId) {
        long jobs = jobRepository.findByEmployerId(employerId).size();
        long apps = applicationRepository.count();
        long hires = applicationRepository
                .findAll()
                .stream()
                .filter(a -> a.getStatus() == ApplicationStatus.HIRED)
                .count();

        return new EmployerStats(jobs, apps, hires);
    }


}
