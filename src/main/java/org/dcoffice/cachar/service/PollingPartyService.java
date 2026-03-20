package org.dcoffice.cachar.service;

import org.dcoffice.cachar.entity.PollingParty;
import org.dcoffice.cachar.repository.PollingPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollingPartyService {

    @Autowired
    private PollingPartyRepository pollingPartyRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<PollingParty> searchPollingParties(String psName, String partyNo, String mobile) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (hasText(psName)) {
            criteriaList.add(Criteria.where("psName")
                    .is(psName.trim()));
        }

        if (hasText(partyNo)) {
            criteriaList.add(Criteria.where("partyNo")
                    .is(partyNo.trim()));
        }

        if (hasText(mobile)) {
            criteriaList.add(Criteria.where("mobile")
                    .is(mobile.trim()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        query.with(Sort.by(Sort.Direction.ASC, "psNo"));

        if (criteriaList.isEmpty()) {
            return pollingPartyRepository.findAll(Sort.by(Sort.Direction.ASC, "psNo"));
        }

        return mongoTemplate.find(query, PollingParty.class);
    }

    public List<String> getAllPollingStations() {
        return mongoTemplate
                .query(PollingParty.class)
                .distinct("psName")
                .as(String.class)
                .all()
                .stream()
                .filter(this::hasText)
                .map(String::trim)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    public List<String> getAllPartyNames() {
        return mongoTemplate
                .query(PollingParty.class)
                .distinct("partyNo")
                .as(String.class)
                .all()
                .stream()
                .filter(this::hasText)
                .map(String::trim)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
