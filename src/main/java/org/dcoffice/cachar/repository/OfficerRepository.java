package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.Officer;
import org.dcoffice.cachar.entity.OfficerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {
    Optional<Officer> findByEmployeeId(String employeeId);
    List<Officer> findByRoleAndIsActiveTrue(OfficerRole role);
    List<Officer> findByIsActiveTrue();
    boolean existsByEmployeeId(String employeeId);
    List<Officer> findByDepartmentAndIsActiveTrue(String department);
}
