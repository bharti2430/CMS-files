package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

    private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);

    private final ComplaintRepository complaintRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public void saveComplaint(Complaint complaint) {
        try {
            complaintRepository.save(complaint);
            logger.info("Complaint registered successfully for student: {}", complaint.getEnrollmentNumber());
        } catch (Exception e) {
            logger.error("Failed to register complaint: {}", e.getMessage());
            throw new RuntimeException("Failed to register complaint", e);
        }
    }

    // Additional methods to fetch complaints, etc., can be added here
}
