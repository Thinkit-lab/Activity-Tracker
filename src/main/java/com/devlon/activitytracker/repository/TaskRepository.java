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

    @Modifying
    @Transactional
    @Query(value = "UPDATE task t set t.status = (t.status - 1) WHERE t.task_id = ?1",
            nativeQuery = true)
    void moveBack(Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE task t set t.title = ?1, t.description = ?2 WHERE t.task_id = ?3",
            nativeQuery = true)
    void updateTask(String title, String description, Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE task t set t.completed_at = ?1 WHERE t.task_id = ?2",
            nativeQuery = true)
    void updateCompletedAt(LocalDateTime now, Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE task t set t.completed_at = null  WHERE t.task_id = ?1",
            nativeQuery = true)
    void removeCompletedAt(Long taskId);

    @Query(value = "select title from task t where t.user_id = ?1",
            nativeQuery = true)
    List<String> findAllTitle(Long userId);
}
