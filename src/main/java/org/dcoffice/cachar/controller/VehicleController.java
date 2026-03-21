package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.service.VehicleExcelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleExcelService service;

    public VehicleController(VehicleExcelService service) {
        this.service = service;
    }

    // 📤 Upload Vehicle Excel
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVehicleExcel(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try (InputStream inputStream = file.getInputStream()) {

            service.uploadExcel(inputStream);

            return ResponseEntity.ok("Vehicle Excel uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }
}