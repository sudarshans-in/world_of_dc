package org.dcoffice.cachar.repository;

import org.dcoffice.cachar.entity.Department;
import org.dcoffice.cachar.entity.Task;
import org.dcoffice.cachar.entity.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    Optional<Task> findByTaskNumber(String taskNumber);

    List<Task> findByCreatedById(String createdById);

    List<Task> findByAssignedToId(String assignedToId);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByDepartment(Department department);

    List<Task> findByAssignedToIdAndStatus(String assignedToId, TaskStatus status);

    @Query("{ 'dueDate': { $lt: ?0 }, 'status': { $nin: ['COMPLETED', 'CANCELLED'] } }")
    List<Task> findOverdueTasks(LocalDateTime now);
}
