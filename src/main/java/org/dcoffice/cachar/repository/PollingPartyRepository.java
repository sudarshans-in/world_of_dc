package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.PollingParty;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollingPartyRepository
        extends MongoRepository<PollingParty, String> {
}