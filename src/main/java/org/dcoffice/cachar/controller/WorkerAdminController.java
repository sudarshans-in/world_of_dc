package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.dto.AdminWorkerSummaryDto;
import org.dcoffice.cachar.dto.WorkerUserDto;
import org.dcoffice.cachar.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class WorkerAdminController {

    private static final Logger logger = LoggerFactory.getLogger(WorkerAdminController.class);

    @Autowired
    private WorkerService workerService;

    @GetMapping("/admin/workers")
    public ResponseEntity<?> getAllWorkers(Authentication authentication) {
        if (!isWorkerAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody("Forbidden"));
        }
        try {
            List<WorkerUserDto> workers = workerService.getAllWorkers();
            return ResponseEntity.ok(workers);
        } catch (Exception e) {
            logger.error("Get all workers failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(e.getMessage()));
        }
    }

    @GetMapping("/admin/attendance/today")
    public ResponseEntity<?> getTodayAttendance(Authentication authentication) {
        if (!isWorkerAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody("Forbidden"));
        }
        try {
            List<AdminWorkerSummaryDto> summary = workerService.getAllTodayAttendance();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Get today attendance summary failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(e.getMessage()));
        }
    }

    private boolean isWorkerAdmin(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_WORKER_ADMIN".equals(a.getAuthority()));
    }

    private Map<String, String> errorBody(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return body;
    }
}
