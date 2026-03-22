package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.dto.ApiResponse;
import org.dcoffice.cachar.dto.ElectionComplaintCreateRequest;
import org.dcoffice.cachar.entity.ElectionComplaint;
import org.dcoffice.cachar.service.ElectionComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/election-complaints")
@CrossOrigin(origins = "*")
public class ElectionComplaintController {

    private final ElectionComplaintService electionComplaintService;

    public ElectionComplaintController(ElectionComplaintService electionComplaintService) {
        this.electionComplaintService = electionComplaintService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ElectionComplaint>>> getByPsName(
            @RequestParam String psName) {
        if (psName == null || psName.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("psName is required"));
        }

        try {
            List<ElectionComplaint> complaints = electionComplaintService.getByPsName(psName.trim());
            return ResponseEntity.ok(ApiResponse.success("Election complaints fetched successfully", complaints));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch election complaints: " + e.getMessage()));
        }
    }

    @PostMapping({"", "/create"})
    public ResponseEntity<ApiResponse<ElectionComplaint>> createElectionComplaint(
            @Valid @RequestBody ElectionComplaintCreateRequest request,
            org.springframework.validation.BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(ApiResponse.error(message));
        }

        try {
            ElectionComplaint created = electionComplaintService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Election complaint created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create election complaint: " + e.getMessage()));
        }
    }
}
