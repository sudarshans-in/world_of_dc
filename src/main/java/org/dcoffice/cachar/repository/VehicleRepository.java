package org.dcoffice.cachar.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.dcoffice.cachar.entity.VehicleDetails;

public interface VehicleRepository extends MongoRepository<VehicleDetails, String> {

    List<VehicleDetails> findByAcNoAndPsNo(String acNo, String psNo);

}