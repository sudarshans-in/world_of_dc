package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.dto.ApiResponse;
import org.dcoffice.cachar.entity.ComplaintDocument;
import org.dcoffice.cachar.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<ApiResponse<List<ComplaintDocument>>> getComplaintFiles(@PathVariable Long complaintId) {
        try {
            List<ComplaintDocument> documents = fileStorageService.getComplaintDocuments(complaintId);
            return ResponseEntity.ok(ApiResponse.success("Complaint files retrieved", documents));
        } catch (Exception e) {
            logger.error("Failed to get files for complaint {}: {}", complaintId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve files: " + e.getMessage()));
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        try {
            byte[] data = fileStorageService.loadFile(fileName);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Failed to download file {}: {}", fileName, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}