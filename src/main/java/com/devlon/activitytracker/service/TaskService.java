package com.devlon.activitytracker.service;

import com.devlon.activitytracker.dto.TaskDTO;
import com.devlon.activitytracker.entity.Task;
import com.devlon.activitytracker.enums.Status;
import com.devlon.activitytracker.exception.CustomUserException;

import java.util.List;

public interface TaskService {
    void saveTask(TaskDTO taskDTO, Long UserId) throws CustomUserException;

    List<TaskDTO> getAllTask(Long userId);

    TaskDTO getTaskById(Long taskId) throws CustomUserException;

    List<TaskDTO> getTasksByPendingStatus(Long userId);

    void moveTask(Long taskId) throws CustomUserException;

    List<TaskDTO> getTasksInProgressStatus(Long userId);

    List<TaskDTO> getAllCompletedTask(Long userId);

    void moveBack(Long taskId) throws CustomUserException;

    void deleteTask(Long taskId);
}
