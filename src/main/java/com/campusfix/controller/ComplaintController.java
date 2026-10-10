package com.campusfix.backend.controller;

import com.campusfix.backend.model.Complaint;
import com.campusfix.backend.repository.ComplaintRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")

@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://24eg107e47.netlify.app"
})

public class ComplaintController {

    private final ComplaintRepository complaintRepository;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

   @PostMapping
public Complaint createComplaint(@RequestBody Complaint complaint) {

    if (complaint.getStatus() == null || complaint.getStatus().isEmpty()) {
        complaint.setStatus("Pending");
    }

    complaint.setCreatedAt(LocalDateTime.now());

    return complaintRepository.save(complaint);
}
    @GetMapping
    public List<Complaint> getComplaints() {
        return complaintRepository.findAll();
    }

    @PutMapping("/{id}/status")
    public Complaint updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String resolutionMessage) {

        Complaint complaint =
                complaintRepository.findById(id).orElseThrow();

        complaint.setStatus(status);

        if ("Resolved".equalsIgnoreCase(status)) {
            complaint.setResolutionMessage(resolutionMessage);
        }

        return complaintRepository.save(complaint);
    }

    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintRepository.deleteById(id);
    }

    @GetMapping("/test")
    public String test() {
        return "Complaint Controller is working";
    }
}