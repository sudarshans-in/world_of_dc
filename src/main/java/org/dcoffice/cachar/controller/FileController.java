package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.dto.ApiResponse;
import org.dcoffice.cachar.entity.Complaint;
import org.dcoffice.cachar.entity.ComplaintDocument;
import org.dcoffice.cachar.service.FileStorageService;
import org.dcoffice.cachar.service.ComplaintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    // Content types that browsers can display inline
    private static final Map<String, String> INLINE_CONTENT_TYPES = Map.ofEntries(
            Map.entry("jpg",  "image/jpeg"),
            Map.entry("jpeg", "image/jpeg"),
            Map.entry("png",  "image/png"),
            Map.entry("gif",  "image/gif"),
            Map.entry("webp", "image/webp"),
            Map.entry("bmp",  "image/bmp"),
            Map.entry("pdf",  "application/pdf"),
            Map.entry("mp4",  "video/mp4"),
            Map.entry("webm", "video/webm"),
            Map.entry("mov",  "video/quicktime")
    );

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<ApiResponse<List<ComplaintDocument>>> getComplaintFiles(@PathVariable Long complaintId) {
        try {
            Optional<Complaint> complaintOpt = complaintService.findByComplaintId(complaintId);
            if (complaintOpt.isPresent()) {
                List<ComplaintDocument> documents = fileStorageService.getComplaintDocuments(complaintOpt.get().getId());
                return ResponseEntity.ok(ApiResponse.success("Complaint files retrieved", documents));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Failed to get files for complaint {}: {}", complaintId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve files: " + e.getMessage()));
        }
    }

    @GetMapping("/download/{*filePath}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String filePath) {
        try {
            // Remove leading slash if present
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }
            logger.info("Attempting to download file: {}", filePath);
            byte[] data = fileStorageService.loadFile(filePath);
            logger.info("Successfully loaded file: {} ({} bytes)", filePath, data.length);
            ByteArrayResource resource = new ByteArrayResource(data);

            String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = fileName.substring(dotIndex + 1).toLowerCase();
            }

            String contentType = INLINE_CONTENT_TYPES.get(extension);
            if (contentType != null) {
                // Serve inline so the browser can display images, PDFs and videos
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                // Serve as a forced download for all other file types
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            }
        } catch (Exception e) {
            logger.error("Failed to download file {}: {}", filePath, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}