package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.PollingParty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollingPartyRepository extends MongoRepository<PollingParty, String> {

    java.util.List<PollingParty> findByPsName(String psName);
}
