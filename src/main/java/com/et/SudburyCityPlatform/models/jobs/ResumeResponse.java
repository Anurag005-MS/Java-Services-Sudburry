package com.et.SudburyCityPlatform.models.jobs;


import lombok.Data;
import java.util.List;

@Data
public class ResumeResponse {

    private String name;
    private List<String> skills;
    private String experience;
    private List<String> projects;
    private String rawText;

    private String Achivements;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getAchivements() {
        return Achivements;
    }

    public void setAchivements(String achivements) {
        Achivements = achivements;
    }
}

