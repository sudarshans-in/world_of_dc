package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.dto.AttendanceRecordDto;
import org.dcoffice.cachar.dto.WorkerAttendanceRequest;
import org.dcoffice.cachar.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class WorkerAttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(WorkerAttendanceController.class);

    @Autowired
    private WorkerService workerService;

    @PostMapping("/attendance/login")
    public ResponseEntity<?> markLogin(@RequestBody WorkerAttendanceRequest request) {
        try {
            AttendanceRecordDto record = workerService.markLogin(request.getUserId(), request.getLocation());
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(e.getMessage()));
        } catch (Exception e) {
            logger.error("Attendance login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(e.getMessage()));
        }
    }

    @PostMapping("/attendance/logout")
    public ResponseEntity<?> markLogout(@RequestBody WorkerAttendanceRequest request) {
        try {
            AttendanceRecordDto record = workerService.markLogout(request.getUserId(), request.getLocation());
            return ResponseEntity.ok(record);
        } catch (IllegalStateException e) {
            // No login exists for today
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(e.getMessage()));
        } catch (Exception e) {
            logger.error("Attendance logout failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(e.getMessage()));
        }
    }

    @GetMapping("/attendance/today/{userId}")
    public ResponseEntity<?> getTodayAttendance(@PathVariable String userId) {
        try {
            AttendanceRecordDto record = workerService.getTodayAttendance(userId);
            if (record == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorBody("No attendance record for today"));
            }
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(e.getMessage()));
        } catch (Exception e) {
            logger.error("Get today attendance failed for {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(e.getMessage()));
        }
    }

    @GetMapping("/attendance/history/{userId}")
    public ResponseEntity<?> getAttendanceHistory(@PathVariable String userId) {
        try {
            List<AttendanceRecordDto> history = workerService.getAttendanceHistory(userId);
            return ResponseEntity.ok(history);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(e.getMessage()));
        } catch (Exception e) {
            logger.error("Get attendance history failed for {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(e.getMessage()));
        }
    }

    private Map<String, String> errorBody(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return body;
    }
}
