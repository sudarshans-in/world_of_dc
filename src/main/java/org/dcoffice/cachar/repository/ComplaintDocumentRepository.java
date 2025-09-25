package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.ComplaintDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintDocumentRepository extends JpaRepository<ComplaintDocument, Long> {
    List<ComplaintDocument> findByComplaintId(Long complaintId);
}
