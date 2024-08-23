package com.example;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// Repository interface for Complaint entities
public interface ComplaintRepository extends MongoRepository<Complaint, String> {

    // Custom method to find complaints by student ID
    List<Complaint> findByEnrollmentNumber(String enrollmentNumber);

    // Custom method to find complaints by complaint type
    List<Complaint> findByComplaintType(String complaintType);
}
