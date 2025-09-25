package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.Complaint;
import org.dcoffice.cachar.entity.ComplaintCategory;
import org.dcoffice.cachar.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findByComplaintNumber(String complaintNumber);
    List<Complaint> findByCitizenId(Long citizenId);
    List<Complaint> findByAssignedToId(Long officerId);
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByCategory(ComplaintCategory category);

    @Query("SELECT c FROM Complaint c WHERE c.assignedTo IS NULL AND c.status = 'SUBMITTED'")
    List<Complaint> findUnassignedComplaints();

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.assignedTo.id = :officerId AND c.status IN ('ASSIGNED', 'IN_PROGRESS')")
    Long countActiveComplaintsByOfficer(@Param("officerId") Long officerId);

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    Long countComplaintsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Complaint c WHERE c.createdAt >= :date ORDER BY c.createdAt DESC")
    List<Complaint> findRecentComplaints(@Param("date") LocalDateTime date);
}
