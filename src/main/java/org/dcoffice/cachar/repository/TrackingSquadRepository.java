package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.TrackingSquad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingSquadRepository extends MongoRepository<TrackingSquad, String> {
}
