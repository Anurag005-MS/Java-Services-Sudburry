package com.et.SudburyCityPlatform.controller;

import com.et.SudburyCityPlatform.models.jobs.Job;
import com.et.SudburyCityPlatform.models.jobs.ResumeResponse;
import com.et.SudburyCityPlatform.service.Jobs.JobService;
import com.et.SudburyCityPlatform.service.ResumeParserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public JobController(JobService jobService, ResumeParserService resumeParserService) {
        this.jobService = jobService;
        this.resumeParserService = resumeParserService;
    }

    @PostMapping("/job")
    @Operation(
            summary = "Create a Job",
            description = "Adds a new job opening with role, location, salary, and requirements"
    )
    public ResponseEntity<Job> addJob(@RequestBody Job job) {
        Job createdJob = jobService.createJob(job);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping("/job")
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
}
