package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.dto.WorkPhotoDto;
import org.dcoffice.cachar.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class WorkerPhotoController {

    private static final Logger logger = LoggerFactory.getLogger(WorkerPhotoController.class);

    @Autowired
    private WorkerService workerService;

    @PostMapping(value = "/photos/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("userId") String userId,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "accuracy", required = false) Double accuracy,
            @RequestParam("photo") MultipartFile photo) {
        try {
            WorkPhotoDto result = workerService.uploadPhoto(userId, photo, notes, latitude, longitude, accuracy);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(e.getMessage()));
        } catch (Exception e) {
            logger.error("Photo upload failed for {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(e.getMessage()));
        }
    }

    @GetMapping("/photos/today/{userId}")
    public ResponseEntity<?> getTodayPhotos(@PathVariable String userId) {
        try {
            List<WorkPhotoDto> photos = workerService.getTodayPhotos(userId);
            return ResponseEntity.ok(photos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(e.getMessage()));
        } catch (Exception e) {
            logger.error("Get today photos failed for {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(e.getMessage()));
        }
    }

    private Map<String, String> errorBody(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return body;
    }
}
