package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.TrackingActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingActivityRepository extends MongoRepository<TrackingActivity, String> {

    Page<TrackingActivity> findBySquadId(String squadId, Pageable pageable);

    Page<TrackingActivity> findBySquadIdAndMemberId(String squadId, String memberId, Pageable pageable);
}
