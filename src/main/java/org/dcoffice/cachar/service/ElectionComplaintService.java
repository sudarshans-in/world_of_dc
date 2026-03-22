package org.dcoffice.cachar.service;

import org.dcoffice.cachar.dto.ElectionComplaintCreateRequest;
import org.dcoffice.cachar.entity.ElectionComplaint;
import org.dcoffice.cachar.repository.ElectionComplaintRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ElectionComplaintService {

    private final ElectionComplaintRepository electionComplaintRepository;

    public ElectionComplaintService(ElectionComplaintRepository electionComplaintRepository) {
        this.electionComplaintRepository = electionComplaintRepository;
    }

    public ElectionComplaint create(ElectionComplaintCreateRequest request) {
        ElectionComplaint complaint = new ElectionComplaint();
        complaint.setName(request.getName().trim());
        complaint.setMobileNo(request.getMobileNo().trim());
        complaint.setPsName(request.getPsName().trim());
        complaint.setSeverity(request.getSeverity().trim());
        complaint.setTitle(request.getTitle().trim());
        complaint.setDescription(request.getDescription().trim());
        complaint.setCreatedAt(System.currentTimeMillis());

        return electionComplaintRepository.save(complaint);
    }

    public List<ElectionComplaint> getByPsName(String psName) {
        return electionComplaintRepository.findByPsName(psName).stream()
                .sorted(Comparator.comparing(ElectionComplaint::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }
}
