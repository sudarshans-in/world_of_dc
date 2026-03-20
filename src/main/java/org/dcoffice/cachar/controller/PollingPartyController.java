package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.service.PollingPartyExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/polling-party")
@CrossOrigin("*")
public class PollingPartyController {

    private static final Logger logger =
            LoggerFactory.getLogger(PollingPartyController.class);

    private final PollingPartyExcelService service;

    public PollingPartyController(PollingPartyExcelService service) {
        this.service = service;
    }

    // =========================================================
    // 🔹 UPLOAD ENCORE OUTPUT (NO RANDOMISATION LOGIC)
    // =========================================================

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Please upload a valid Excel file");
        }

        try {
            logger.info("Uploading ENCORE Excel: {}", file.getOriginalFilename());

            service.uploadExcel(file.getInputStream());

            return ResponseEntity.ok(
                    "Excel uploaded successfully to MongoDB");

        } catch (Exception e) {
            logger.error("Upload failed", e);

            return ResponseEntity.status(500)
                    .body("Error: " + e.getMessage());
        }
    }
}