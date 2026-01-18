package com.et.SudburyCityPlatform.controller;

import com.et.SudburyCityPlatform.models.jobs.JobSeekerProfile;
import com.et.SudburyCityPlatform.service.Jobs.JobSeekerProfileService;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/jobseeker/profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService service;

    public JobSeekerProfileController(JobSeekerProfileService service) {
        this.service = service;
    }

    @PostMapping
    public JobSeekerProfile save(
            @RequestParam String email,
            @RequestBody JobSeekerProfile profile) {

        return service.save(email, profile);
    }

    @GetMapping
    public JobSeekerProfile get(@RequestParam String email) {
        return service.get(email);
    }

    @GetMapping("/completion")
    public int completion(@RequestParam String email) {
        return service.completion(email);
    }
}


