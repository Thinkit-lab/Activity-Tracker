package com.devlon.activitytracker.service.implementation;

import com.devlon.activitytracker.dto.TaskDTO;
import com.devlon.activitytracker.entity.Task;
import com.devlon.activitytracker.entity.User;
import com.devlon.activitytracker.enums.Status;
import com.devlon.activitytracker.exception.CustomUserException;
import com.devlon.activitytracker.repository.TaskRepository;
import com.devlon.activitytracker.repository.UserRepository;
import com.devlon.activitytracker.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveTask(TaskDTO taskDTO, Long userId) throws CustomUserException {

        Optional<User> userOptional = userRepository.findById(userId);
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            throw new CustomUserException("User not found");
        }

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .build();
        task.setCompletedAt(task.getCompletedAt());
        task.setStatus(Status.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUser(user);

        taskRepository.save(task);
    }

    @Override
    public List<TaskDTO> getAllTask(Long userId) {
        List<Task> tasks = taskRepository.findAllByUserUserId(userId);
        System.out.println(tasks.size());

        return tasks.stream().map(this::mappedToDTO).toList();
    }

    @Override
    public TaskDTO getTaskById(Long taskId) throws CustomUserException {
        Optional<Task> task = taskRepository.findById(taskId);
        if(task.isPresent()) {
            return mappedToDTO(task.get());
        }
        throw new CustomUserException("Task is empty");
    }

    @Override
    public List<TaskDTO> getTasksByPendingStatus(Long userId) {
        List<Task> tasks = taskRepository.findAllByStatusPending(userId);
        return tasks.stream().map(this::mappedToDTO).toList();
    }

    @Override
    @Transactional
    public void moveTask(Long taskId) throws CustomUserException {

        Task task = taskRepository.findById(taskId).get();
        System.out.println("Task is " + task);
        if(task.getStatus() == Status.DONE){
            throw new CustomUserException("Task has been completed and cannot be moved further. " +
                    "You can consider moving it back");
        }
        taskRepository.moveTask(taskId);

        taskRepository.updateUpdateAt(LocalDateTime.now(), taskId);

       if(task.getStatus() == Status.IN_PROGRESS){
           taskRepository.updateCompletedAt(LocalDateTime.now(), taskId);
       }
    }

    @Override
    public List<TaskDTO> getTasksInProgressStatus(Long userId) {

        List<Task> tasks = taskRepository.findAllInProgress(userId);
        return tasks.stream().map(this::mappedToDTO).toList();
    }

    @Override
    public List<TaskDTO> getAllCompletedTask(Long userId) {
        List<Task> tasks = taskRepository.findAllDone(userId);
        return tasks.stream().map(this::mappedToDTO).toList();
    }

    @Override
    public void moveBack(Long taskId) throws CustomUserException {
        Task task = taskRepository.findById(taskId).get();
        if(task.getStatus() == Status.PENDING){
            throw new CustomUserException("Task cannot be moved further. " +
                    "You can consider moving it up");
        }
        taskRepository.moveBack(taskId);

        taskRepository.updateUpdateAt(LocalDateTime.now(), taskId);

        if(task.getStatus() == Status.DONE) {
            taskRepository.removeCompletedAt(taskId);
        }
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public void editTask(TaskDTO taskDTO, Long taskId) {
        try {
            taskRepository.updateTask(taskDTO.getTitle(), taskDTO.getDescription(), taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        taskRepository.updateUpdateAt(LocalDateTime.now(), taskId);
    }

    @Override
    public List<TaskDTO> searchTask(String title, Long userId) {
        List<Task> tasks = taskRepository.findAllByUserUserId(userId);
        List<Task> searhchedTasks;

        searhchedTasks = tasks.stream().filter(task-> task.getTitle().toLowerCase().
                contains(title.toLowerCase())).collect(Collectors.toList());

        return searhchedTasks.stream().map(this::mappedToDTO).collect(Collectors.toList());
    }

    public Page<TaskDTO> findPaginated(List<TaskDTO> tasks, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<TaskDTO> list;

        if (tasks.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tasks.size());
            list = tasks.subList(startItem, toIndex);
        }

        Page<TaskDTO> taskPage
                = new PageImpl<TaskDTO>(list, PageRequest.of(currentPage, pageSize), tasks.size());

        return taskPage;
    }

    private TaskDTO mappedToDTO(Task task) {
        TaskDTO taskDTO = TaskDTO.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setUpdatedAt(task.getUpdatedAt());
        taskDTO.setCompletedAt(task.getCompletedAt());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setTaskId(task.getTaskId());

        return taskDTO;
    }
}
