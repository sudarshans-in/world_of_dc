package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.ComplaintHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintHistoryRepository extends JpaRepository<ComplaintHistory, Long> {
    List<ComplaintHistory> findByComplaintIdOrderByTimestampDesc(Long complaintId);
    List<ComplaintHistory> findByOfficerIdOrderByTimestampDesc(Long officerId);
}
