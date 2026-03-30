package org.dcoffice.cachar.service;

import org.dcoffice.cachar.dto.CreateTrackingMemberRequest;
import org.dcoffice.cachar.dto.CreateTrackingSquadRequest;
import org.dcoffice.cachar.dto.TrackingDashboardItemDto;
import org.dcoffice.cachar.dto.TrackingDashboardResponseDto;
import org.dcoffice.cachar.dto.UpdateTrackingLocationRequest;
import org.dcoffice.cachar.entity.TrackingActivity;
import org.dcoffice.cachar.entity.TrackingMember;
import org.dcoffice.cachar.entity.TrackingSquad;
import org.dcoffice.cachar.repository.TrackingActivityRepository;
import org.dcoffice.cachar.repository.TrackingMemberRepository;
import org.dcoffice.cachar.repository.TrackingSquadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TrackingService {

    private static final int DEFAULT_LOG_LIMIT = 100;
    private static final int MAX_LOG_LIMIT = 500;

    @Autowired
    private TrackingSquadRepository trackingSquadRepository;

    @Autowired
    private TrackingMemberRepository trackingMemberRepository;

    @Autowired
    private TrackingActivityRepository trackingActivityRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public TrackingDashboardResponseDto getDashboard(String squadId, Integer activityLimit) {
        List<TrackingDashboardItemDto> items = new ArrayList<>();
        int resolvedLimit = resolveLimit(activityLimit);

        if (squadId != null && !squadId.trim().isEmpty()) {
            TrackingSquad squad = requireSquad(squadId.trim());
            items.add(buildDashboardItem(squad, resolvedLimit));
        } else {
            List<TrackingSquad> squads = getAllSquads();
            for (TrackingSquad squad : squads) {
                items.add(buildDashboardItem(squad, resolvedLimit));
            }
        }

        TrackingDashboardResponseDto response = new TrackingDashboardResponseDto();
        response.setGeneratedAt(Instant.now());
        response.setSquads(items);
        return response;
    }

    public List<TrackingSquad> getAllSquads() {
        return trackingSquadRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public TrackingSquad createSquad(CreateTrackingSquadRequest request) {
        String squadId = normalizeId(request.getId(), "sq");
        if (trackingSquadRepository.existsById(squadId)) {
            throw new IllegalArgumentException("Squad already exists with id: " + squadId);
        }

        TrackingSquad squad = new TrackingSquad();
        squad.setId(squadId);
        squad.setName(request.getName().trim());
        squad.setZone(request.getZone().trim());
        squad.setLeadId(blankToNull(request.getLeadId()));
        return trackingSquadRepository.save(squad);
    }

    public TrackingMember createMember(CreateTrackingMemberRequest request) {
        String squadId = request.getSquadId().trim();
        requireSquad(squadId);

        String memberId = normalizeId(request.getId(), "mem");
        if (trackingMemberRepository.existsById(memberId)) {
            throw new IllegalArgumentException("Member already exists with id: " + memberId);
        }

        TrackingMember member = new TrackingMember();
        member.setId(memberId);
        member.setSquadId(squadId);
        member.setName(request.getName().trim());
        member.setRole(blankToNull(request.getRole()));
        member.setPhone(blankToNull(request.getPhone()));
        member.setStatus(defaultStatus(request.getStatus()));
        member.setAddress(blankToNull(request.getAddress()));
        member.setCreatedAt(Instant.now());
        if (request.getLatitude() != null && request.getLongitude() != null) {
            member.setLocation(new GeoJsonPoint(request.getLongitude(), request.getLatitude()));
            member.setLastUpdate(Instant.now());
        }

        TrackingMember saved = trackingMemberRepository.save(member);

        if (saved.getAddress() != null) {
            TrackingActivity activity = new TrackingActivity();
            activity.setSquadId(saved.getSquadId());
            activity.setMemberId(saved.getId());
            activity.setMemberName(saved.getName());
            activity.setStatus(saved.getStatus());
            activity.setLocation(saved.getAddress());
            activity.setTimestamp(saved.getLastUpdate() != null ? saved.getLastUpdate() : Instant.now());
            trackingActivityRepository.save(activity);
        }

        return saved;
    }

    public TrackingMember updateMemberLocation(String memberId, UpdateTrackingLocationRequest request) {
        TrackingMember member = trackingMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        member.setLocation(new GeoJsonPoint(request.getLongitude(), request.getLatitude()));
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            member.setAddress(request.getAddress().trim());
        }
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            member.setStatus(request.getStatus().trim());
        }
        member.setLastUpdate(request.getTimestamp() != null ? request.getTimestamp() : Instant.now());

        TrackingMember saved = trackingMemberRepository.save(member);

        TrackingActivity activity = new TrackingActivity();
        activity.setSquadId(saved.getSquadId());
        activity.setMemberId(saved.getId());
        activity.setMemberName(saved.getName());
        activity.setStatus(saved.getStatus());
        activity.setLocation(saved.getAddress());
        activity.setTimestamp(saved.getLastUpdate());
        trackingActivityRepository.save(activity);

        return saved;
    }

    public TrackingMember updateMember(String memberId, CreateTrackingMemberRequest request) {
        TrackingMember member = trackingMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            member.setName(request.getName().trim());
        }
        if (request.getRole() != null) {
            member.setRole(blankToNull(request.getRole()));
        }
        if (request.getPhone() != null) {
            member.setPhone(blankToNull(request.getPhone()));
        }
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            member.setStatus(request.getStatus().trim());
        }
        if (request.getAddress() != null) {
            member.setAddress(blankToNull(request.getAddress()));
        }
        if (request.getLatitude() != null && request.getLongitude() != null) {
            member.setLocation(new GeoJsonPoint(request.getLongitude(), request.getLatitude()));
            member.setLastUpdate(Instant.now());
        }

        TrackingMember saved = trackingMemberRepository.save(member);

        TrackingActivity activity = new TrackingActivity();
        activity.setSquadId(saved.getSquadId());
        activity.setMemberId(saved.getId());
        activity.setMemberName(saved.getName());
        activity.setStatus(saved.getStatus());
        activity.setLocation(saved.getAddress());
        activity.setTimestamp(saved.getLastUpdate() != null ? saved.getLastUpdate() : Instant.now());
        trackingActivityRepository.save(activity);

        return saved;
    }

    public void deleteMember(String memberId) {
        TrackingMember member = trackingMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        trackingMemberRepository.delete(member);
    }

    public List<TrackingMember> getMembersBySquadId(String squadId) {
        requireSquad(squadId);
        return trackingMemberRepository.findBySquadIdOrderByNameAsc(squadId);
    }

    public List<TrackingActivity> getActivityBySquadId(String squadId, Integer limit) {
        requireSquad(squadId);
        Pageable pageable = activityPageable(limit);
        return trackingActivityRepository.findBySquadId(squadId, pageable).getContent();
    }

    public List<TrackingActivity> getActivityBySquadAndMember(String squadId, String memberId, Integer limit) {
        requireSquad(squadId);
        trackingMemberRepository.findByIdAndSquadId(memberId, squadId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found for squadId=" + squadId + " and memberId=" + memberId));

        Pageable pageable = activityPageable(limit);
        return trackingActivityRepository.findBySquadIdAndMemberId(squadId, memberId, pageable).getContent();
    }

    public TrackingActivity getActivity(String activityId) {
        return trackingActivityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with id: " + activityId));
    }

    public TrackingActivity uploadActivityAttachments(String activityId, List<MultipartFile> files) {
        TrackingActivity activity = getActivity(activityId);
        List<String> attachments = activity.getAttachments();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                try {
                    String path = fileStorageService.storeFile(file, "tracking/activities");
                    attachments.add(path);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to store file: " + file.getOriginalFilename() + " — " + e.getMessage());
                }
            }
        }

        activity.setAttachments(attachments);
        return trackingActivityRepository.save(activity);
    }

    public TrackingActivity deleteActivityAttachment(String activityId, String filePath) {
        TrackingActivity activity = getActivity(activityId);
        List<String> attachments = activity.getAttachments();

        if (!attachments.remove(filePath)) {
            throw new IllegalArgumentException("Attachment not found in activity: " + filePath);
        }

        fileStorageService.deleteFile(filePath);
        activity.setAttachments(attachments);
        return trackingActivityRepository.save(activity);
    }

    private TrackingSquad requireSquad(String squadId) {
        return trackingSquadRepository.findById(squadId)
                .orElseThrow(() -> new IllegalArgumentException("Squad not found with id: " + squadId));
    }

    private Pageable activityPageable(Integer limit) {
        int resolvedLimit = resolveLimit(limit);
        if (resolvedLimit < 1 || resolvedLimit > MAX_LOG_LIMIT) {
            throw new IllegalArgumentException("limit must be between 1 and " + MAX_LOG_LIMIT);
        }
        return PageRequest.of(0, resolvedLimit, Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    private TrackingDashboardItemDto buildDashboardItem(TrackingSquad squad, int activityLimit) {
        TrackingDashboardItemDto item = new TrackingDashboardItemDto();
        item.setSquad(squad);
        item.setMembers(trackingMemberRepository.findBySquadIdOrderByNameAsc(squad.getId()));

        Page<TrackingActivity> latest = trackingActivityRepository.findBySquadId(
                squad.getId(),
                PageRequest.of(0, activityLimit, Sort.by(Sort.Direction.DESC, "timestamp"))
        );
        item.setLatestActivities(latest.getContent());
        return item;
    }

    private int resolveLimit(Integer limit) {
        int resolved = (limit == null) ? DEFAULT_LOG_LIMIT : limit;
        if (resolved < 1 || resolved > MAX_LOG_LIMIT) {
            throw new IllegalArgumentException("limit must be between 1 and " + MAX_LOG_LIMIT);
        }
        return resolved;
    }

    private String normalizeId(String id, String prefix) {
        if (id != null && !id.trim().isEmpty()) {
            return id.trim();
        }
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String blankToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private String defaultStatus(String status) {
        return status == null || status.trim().isEmpty() ? "ACTIVE" : status.trim();
    }
}
