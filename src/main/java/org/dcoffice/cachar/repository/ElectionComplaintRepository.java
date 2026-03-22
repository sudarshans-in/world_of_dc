package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.ElectionComplaint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ElectionComplaintRepository extends MongoRepository<ElectionComplaint, String> {
	List<ElectionComplaint> findByPsName(String psName);
}
