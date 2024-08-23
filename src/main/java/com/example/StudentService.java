package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StudentService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    @Autowired
    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return student.get();
        } else {
            logger.warn("User with email {} not found", email);
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void registerUser(Student student) {
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            logger.warn("User with email {} already exists", student.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }
        studentRepository.save(student);
        logger.info("User with email {} registered successfully", student.getEmail());
    }
    
    public Student findStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found"));
    }

    public void updateStudent(Student student) {
        studentRepository.save(student);
        logger.info("Student with email {} updated successfully", student.getEmail());
    }
}
