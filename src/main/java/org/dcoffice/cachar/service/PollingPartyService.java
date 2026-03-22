package org.dcoffice.cachar.service;

import org.dcoffice.cachar.entity.Materials;
import org.dcoffice.cachar.entity.Member;
import org.dcoffice.cachar.entity.PollingParty;
import org.dcoffice.cachar.repository.PollingPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PollingPartyService {

    @Autowired
    private PollingPartyRepository pollingPartyRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<PollingParty> searchPollingParties(String psName, String mobile) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (hasText(psName)) {
            criteriaList.add(Criteria.where("psName")
                    .is(psName.trim()));
        }

        if (hasText(mobile)) {
            criteriaList.add(Criteria.where("members.mobile")
                    .is(mobile.trim()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        query.with(Sort.by(Sort.Direction.ASC, "psNo"));

        if (criteriaList.isEmpty()) {
            return pollingPartyRepository.findAll(Sort.by(Sort.Direction.ASC, "psNo"));
        }

        List<PollingParty> results = mongoTemplate.find(query, PollingParty.class);

        // Guard against duplicate documents if data/query expansion ever returns repeated IDs.
        Map<String, PollingParty> distinctById = new LinkedHashMap<>();
        for (PollingParty party : results) {
            if (party.getId() != null) {
                distinctById.putIfAbsent(party.getId(), party);
            }
        }
        return new ArrayList<>(distinctById.values());
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

    public java.util.List<Materials> getMaterialsByPsName(String psName) {
        java.util.List<PollingParty> parties = pollingPartyRepository.findByPsName(psName);
        if (parties.isEmpty()) {
            throw new RuntimeException("No polling party found for psName: " + psName);
        }
        return parties.stream()
                .map(p -> p.getMaterials() != null ? p.getMaterials() : Materials.defaultMaterials())
                .collect(Collectors.toList());
    }

    public java.util.List<Materials> updateMaterialsByPsName(String psName, Materials materials) {
        java.util.List<PollingParty> parties = pollingPartyRepository.findByPsName(psName);
        if (parties.isEmpty()) {
            throw new RuntimeException("No polling party found for psName: " + psName);
        }
        materials.setSubmitted(true); // Ensure submitted is true if we're updating materials
        if (materials.isSubmitted() && materials.getSubmittedAt() == null) {
            materials.setSubmittedAt(System.currentTimeMillis());
        }
        parties.forEach(p -> p.setMaterials(materials));
        pollingPartyRepository.saveAll(parties);
        return parties.stream().map(PollingParty::getMaterials).collect(Collectors.toList());
    }

    public java.util.List<Member> getMembersByPsName(String psName) {
        java.util.List<PollingParty> parties = pollingPartyRepository.findByPsName(psName);
        if (parties.isEmpty()) {
            throw new RuntimeException("No polling party found for psName: " + psName);
        }
        return parties.stream()
                .filter(p -> p.getMembers() != null)
                .flatMap(p -> p.getMembers().stream())
                .collect(Collectors.toList());
    }

    public java.util.List<Member> updateMembersByPsName(String psName, java.util.List<Member> members) {
        java.util.List<PollingParty> parties = pollingPartyRepository.findByPsName(psName);
        if (parties.isEmpty()) {
            throw new RuntimeException("No polling party found for psName: " + psName);
        }
        parties.forEach(p -> p.setMembers(members));
        pollingPartyRepository.saveAll(parties);
        return members;
    }

    public String getVehicleIdByPsName(String psName) {
        java.util.List<PollingParty> parties = pollingPartyRepository.findByPsName(psName);
        if (parties.isEmpty()) {
            throw new RuntimeException("No polling party found for psName: " + psName);
        }
        return parties.get(0).getVehicleId();
    }

    public String updateVehicleIdByPsName(String psName, String vehicleId) {
        java.util.List<PollingParty> parties = pollingPartyRepository.findByPsName(psName);
        if (parties.isEmpty()) {
            throw new RuntimeException("No polling party found for psName: " + psName);
        }
        parties.forEach(p -> p.setVehicleId(vehicleId));
        pollingPartyRepository.saveAll(parties);
        return vehicleId;
    }
}
