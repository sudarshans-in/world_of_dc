package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.dto.ApiResponse;
import org.dcoffice.cachar.dto.PollingPartyOptionsDto;
import org.dcoffice.cachar.entity.PollingParty;
import org.dcoffice.cachar.service.PollingPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/polling-parties")
@CrossOrigin(origins = "*")
public class PollingPartyController {

    private static final Logger logger = LoggerFactory.getLogger(PollingPartyController.class);

    @Autowired
    private PollingPartyService pollingPartyService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PollingParty>>> searchPollingParties(
            @RequestParam(value = "psName", required = false) String psName,
            @RequestParam(value = "partyNo", required = false) String partyNo,
            @RequestParam(value = "mobile", required = false) String mobile) {
        try {
            List<PollingParty> pollingParties = pollingPartyService.searchPollingParties(psName, partyNo, mobile);
            return ResponseEntity.ok(ApiResponse.success("Polling parties retrieved successfully", pollingParties));
        } catch (Exception e) {
            logger.error("Failed to search polling parties: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to search polling parties: " + e.getMessage()));
        }
    }

    @GetMapping("/options")
    public ResponseEntity<ApiResponse<PollingPartyOptionsDto>> getPollingPartyOptions() {
        try {
            PollingPartyOptionsDto options = new PollingPartyOptionsDto(
                    pollingPartyService.getAllPollingStations(),
                    pollingPartyService.getAllPartyNames());
            return ResponseEntity.ok(ApiResponse.success("Polling party options retrieved successfully", options));
        } catch (Exception e) {
            logger.error("Failed to retrieve polling party options: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve polling party options: " + e.getMessage()));
        }
    }
}
