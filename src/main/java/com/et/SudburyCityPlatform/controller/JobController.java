package com.et.SudburyCityPlatform.controller;

import com.et.SudburyCityPlatform.models.jobs.*;
import com.et.SudburyCityPlatform.service.Jobs.JobService;
import com.et.SudburyCityPlatform.service.Jobs.SavedJobService;
import com.et.SudburyCityPlatform.service.ResumeParserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(
        name = "Job Management",
        description = "APIs for creating, updating, and retrieving job openings"
)
public class JobController {

    private final JobService jobService;

    private final ResumeParserService resumeParserService;
    private final SavedJobService savedJobService;
    public JobController(JobService jobService, ResumeParserService resumeParserService, SavedJobService savedJobService) {
        this.jobService = jobService;
        this.resumeParserService = resumeParserService;
        this.savedJobService = savedJobService;
    }

    @PostMapping("/job")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Job> addJob(
            @RequestBody Job job,
            Authentication auth) {

        Long employerId =
                ((CustomUserDetails) auth.getPrincipal()).getEmployerId();

        job.setEmployer(new Employer(employerId));
        Job createdJob = jobService.createJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @GetMapping("/job")
    @PreAuthorize("hasAnyRole('JOB_SEEKER','EMPLOYER','ADMIN')")
    @Operation(
            summary = "Get All Jobs",
            description = "Fetches all available job openings"
    )
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PutMapping("/job")
    @Operation(
            summary = "Update Job",
            description = "Updates an existing job opening using job ID"
    )
    public ResponseEntity<Job> updateJob(@RequestBody Job job) throws Throwable {
        Job updatedJob = jobService.updateJob(job);
        return new ResponseEntity<>(updatedJob, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file) throws Exception {

        return ResponseEntity.ok(resumeParserService.parseResume(file));
    }
    @PostMapping("/{jobId}/apply")
    public ResponseEntity<JobApplicationRequest> applyForJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobApplicationRequest request) {

        JobApplicationRequest saved =
                jobService.applyForJob(jobId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @GetMapping("/employer/jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<Job> getEmployerJobs(Authentication auth) {
        Long employerId =  ((CustomUserDetails)auth.getPrincipal()).getEmployerId();
        return jobService.getJobsByEmployer(employerId);
    }

    @GetMapping("/employer/jobs/{jobId}/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<JobApplicationRequest> getApplicantsForJob(
            @PathVariable Long jobId,
            Authentication auth) {

        Long employerId = ((CustomUserDetails) auth.getPrincipal()).getEmployerId();
        return jobService.getApplicationsForJob(jobId, employerId);
    }
    @GetMapping("/employer/applications/{applicationId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public JobApplicationRequest getApplicationDetails(
            @PathVariable Long applicationId,
            Authentication auth) {

        Long employerId = ((CustomUserDetails) auth.getPrincipal()).getEmployerId();
        return jobService.getApplicationDetails(applicationId, employerId);
    }

    @PutMapping("/employer/applications/{applicationId}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public JobApplicationRequest updateStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status,
            Authentication auth) {

        Long employerId = ((CustomUserDetails) auth.getPrincipal()).getEmployerId();
        return jobService.updateApplicationStatus(applicationId, status, employerId);
    }
    @GetMapping("/jobseeker/applications")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public List<JobApplicationRequest> getMyApplications(Authentication auth) {

        String email = auth.getName();
        return jobService.getApplicationsByEmail(email);
    }

    @GetMapping("/search")
    public List<Job> search(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minSalary) {
        return jobService.searchJobs(location, type, minSalary);
    }

    @PostMapping("/jobs/{jobId}/save")
    public void saveJob(
            @PathVariable Long jobId,
            @RequestParam String email) {
        savedJobService.save(email, jobId);
    }

    @GetMapping("/jobs/saved")
    public List<Job> saved(@RequestParam String email) {
        return savedJobService.getSaved(email);
    }
    @GetMapping("/jobs/recommended")
    public List<Job> recommended(@RequestParam String email) {
        return jobService.matchJobsByProfile(email);
    }

}
