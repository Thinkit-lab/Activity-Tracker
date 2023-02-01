package com.devlon.activitytracker.repository;

import com.devlon.activitytracker.entity.Task;
import com.devlon.activitytracker.enums.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "select * from task t where t.user_id = ?1",
            nativeQuery = true)
    List<Task> findAllByUserUserId(Long userId);

    @Query(value = "select * from task t where t.status = 0 and t.user_id = ?1",
            nativeQuery = true)
    List<Task> findAllByStatusPending(Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE task t set t.status = (t.status + 1) WHERE t.task_id = ?1",
            nativeQuery = true)
    void moveTask(Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE task t set t.updated_at = ?1 WHERE t.task_id = ?2",
            nativeQuery = true)
    void updateUpdateAt(LocalDateTime dateTime, Long taskId);

    @Query(value = "select * from task t where t.status = 1 and t.user_id = ?1",
            nativeQuery = true)
    List<Task> findAllInProgress(Long userId);

    @Query(value = "select * from task t where t.status = 2 and t.user_id = ?1",
            nativeQuery = true)
    List<Task> findAllDone(Long userId);
}
