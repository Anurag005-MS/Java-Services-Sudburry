package com.et.SudburyCityPlatform.service.program;

import com.et.SudburyCityPlatform.models.program.*;
import com.et.SudburyCityPlatform.repository.program.ProgramEnrollmentRepository;
import com.et.SudburyCityPlatform.repository.program.ProgramRepository;
import com.et.SudburyCityPlatform.repository.program.UserRepositoryProgram;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository repository;

    private final UserRepositoryProgram userRepository;
    private final ProgramEnrollmentRepository enrollmentRepository;

    public ProgramService(ProgramRepository repository, UserRepositoryProgram userRepository, ProgramEnrollmentRepository enrollmentRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Program create(ProgramRequest request) {
        Program program = new Program();
        map(request, program);
        return repository.save(program);
    }

    public Program update(Long id, ProgramRequest request) {
        Program program = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));
        map(request, program);
        return repository.save(program);
    }

    public List<Program> getAll() {
        return repository.findAll();
    }

    public Program getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void map(ProgramRequest r, Program p) {
        p.setTitle(r.getTitle());
        p.setProvider(r.getProvider());
        p.setCost(r.getCost());
        p.setDuration(r.getDuration());
        p.setStartDate(r.getStartDate());
        p.setAvailableSeats(r.getAvailableSeats());
        p.setDescription(r.getDescription());
        p.setLearningPoints(r.getLearningPoints());
        p.setEligibility(r.getEligibility());
    }
    public void apply(Long programId, ApplyProgramRequest request) {

        // 1️⃣ Program exists?
        Program program = repository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        if (program.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        // 2️⃣ Find or create user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User u = new User();
                    u.setFullName(request.getFullName());
                    u.setEmail(request.getEmail());
                    u.setPhone(request.getPhone());
                    return userRepository.save(u);
                });

        // 3️⃣ Prevent duplicate enrollment
        if (enrollmentRepository.existsByProgramAndUser(program, user)) {
            throw new RuntimeException("Already enrolled in this program");
        }

        // 4️⃣ Save enrollment
        ProgramEnrollment enrollment = new ProgramEnrollment();
        enrollment.setProgram(program);
        enrollment.setUser(user);
        enrollment.setAppliedAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);

        // 5️⃣ Reduce seat count
        program.setAvailableSeats(program.getAvailableSeats() - 1);
        repository.save(program);
    }
}

