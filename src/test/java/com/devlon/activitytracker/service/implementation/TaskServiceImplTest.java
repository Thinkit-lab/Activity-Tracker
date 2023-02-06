package com.devlon.activitytracker.service.implementation;

import com.devlon.activitytracker.dto.TaskDTO;
import com.devlon.activitytracker.entity.Task;
import com.devlon.activitytracker.entity.User;
import com.devlon.activitytracker.enums.Status;
import com.devlon.activitytracker.exception.CustomUserException;
import com.devlon.activitytracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskServiceImpl taskService;
    @MockBean
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .userName("Olu")
                .email("Olu@gmail.com")
                .password("olumide")
                .userId(1L)
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Task task = Task.builder()
                .taskId(1L)
                .title("Title")
                .description("New task")
                .status(Status.PENDING)
                .completedAt(LocalDateTime.now())
                .build();
        task.setUser(user);

        List<Task> tasks = new ArrayList<>(List.of(task));
        List<String> stringList = new ArrayList<>(List.of(task.getTitle()));

        Mockito.when(taskRepository.findAll()).thenReturn(tasks);
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.findAllByStatusPending(1L)).thenReturn(new ArrayList<>());
        Mockito.when(taskRepository.findAllDone(1L)).thenReturn(new ArrayList<>());
        Mockito.when(taskRepository.findAllTitle(1L)).thenReturn(stringList);

    }

    @Test
    @DisplayName("Get all task")
    void getAllTaskTest() {
        List<TaskDTO> tasks = taskService.getAllTask(1L);
        assertNotNull(tasks);
    }

    @Test
    @DisplayName("Get task by Id")
    void getTaskById() throws CustomUserException {
        TaskDTO task = taskService.getTaskById(1L);
        assertNotNull(task);
    }

    @Test
    @DisplayName("Get all task with status pending")
    void getTasksByPendingStatusTest() {
        List<TaskDTO> tasks = taskService.getTasksByPendingStatus(1L);
        assertNotNull(tasks);
    }

    @Test
    @DisplayName("Move task forward")
    void moveTaskTest() {

    }

    @Test
    @DisplayName("Get all task in status progress")
    void getTasksInProgressStatus() {
        List<TaskDTO> tasks = taskService.getTasksInProgressStatus(1L);
        assert(tasks.size() == 0);
    }

    @Test
    @DisplayName("Get all task in status Done")
    void getAllCompletedTask() {
        List<TaskDTO> tasks = taskService.getAllCompletedTask(1L);
        assert(tasks.size() == 0);
    }

    @Test
    void moveBack() {
    }

    @Test
    void deleteTask() {

    }

    @Test
    void editTask() {
    }

    @Test
    void searchTask() {
        List<TaskDTO> searchResult = taskService.searchTask("Title", 1L);
        assertNotNull(searchResult);
    }

    @Test
    void findPaginated() {
    }
}