package com.et.SudburyCityPlatform.service.Jobs;

import com.et.SudburyCityPlatform.models.jobs.JobSeekerProfile;
import com.et.SudburyCityPlatform.repository.Jobs.JobSeekerProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository repo;

    public JobSeekerProfileService(JobSeekerProfileRepository repo) {
        this.repo = repo;
    }

    public JobSeekerProfile save(String email, JobSeekerProfile profile) {
        JobSeekerProfile p =
                repo.findByEmail(email).orElse(new JobSeekerProfile());

        p.setEmail(email);
        p.setFullName(profile.getFullName());
        p.setCity(profile.getCity());
        p.setPostalCode(profile.getPostalCode());
        p.setSummary(profile.getSummary());
        p.setSkills(profile.getSkills());
        p.setYearsOfExperience(profile.getYearsOfExperience());
        p.setResumeUrl(profile.getResumeUrl());

        return repo.save(p);
    }

    public JobSeekerProfile get(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }
    public int completion(String email) {
        JobSeekerProfile p = get(email);

        int filled = 0;
        int total = 6;

        if (p.getFullName() != null) filled++;
        if (p.getCity() != null) filled++;
        if (p.getSummary() != null) filled++;
        if (p.getSkills() != null && !p.getSkills().isEmpty()) filled++;
        if (p.getResumeUrl() != null) filled++;
        if (p.getYearsOfExperience() != null) filled++;

        return (filled * 100) / total;
    }
}

