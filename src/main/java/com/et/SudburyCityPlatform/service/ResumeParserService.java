package com.et.SudburyCityPlatform.service;

import com.et.SudburyCityPlatform.models.jobs.ResumeResponse;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResumeParserService {

    private final Tika tika = new Tika();

    public ResumeResponse parseResume(MultipartFile file) throws Exception {

        String text = tika.parseToString(file.getInputStream());

        ResumeResponse response = new ResumeResponse();
        response.setRawText(text);
        response.setName(extractName(text));

        response.setSkills(extractSkills(text));
        response.setExperience(extractSection(text, "EXPERIENCE"));
        response.setProjects(extractProjects(text));
        response.setAchivements(extractSection(text, "ACHIEVEMENTS"));

        return response;
    }

    // ---------- NAME ----------
    private String extractName(String text) {
        return Arrays.stream(text.split("\n"))
                .map(String::trim)
                .filter(line -> line.length() > 3 && line.length() < 50)
                .findFirst()
                .orElse("Not Found");
    }

    // ---------- SKILLS ----------
    private List<String> extractSkills(String text) {
        String skillsBlock = extractSection(text, "SKILLS");
        if (skillsBlock == null) return List.of();

        return Arrays.stream(skillsBlock.split(",|\n|•|-"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    // ---------- PROJECTS ----------
    private List<String> extractProjects(String text) {
        String projectBlock = extractSection(text, "PROJECTS");
        if (projectBlock == null) return List.of();

        return Arrays.stream(projectBlock.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    // ---------- GENERIC SECTION EXTRACTOR ----------
    private String extractSection(String text, String sectionName) {

        Pattern pattern = Pattern.compile(
                sectionName + "\\s*[:\\n](.*?)(?=\\n[A-Z ]{3,}|$)",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group(1).trim() : null;
    }
}
