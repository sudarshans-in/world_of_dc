package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.TrackingMember;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingMemberRepository extends MongoRepository<TrackingMember, String> {

    List<TrackingMember> findBySquadIdOrderByNameAsc(String squadId);

    List<TrackingMember> findAllByOrderByNameAsc();

    Optional<TrackingMember> findByIdAndSquadId(String id, String squadId);

    Optional<TrackingMember> findByPhone(String phone);
}
