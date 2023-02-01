package com.devlon.activitytracker.controller;

import com.devlon.activitytracker.dto.TaskDTO;
import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.enums.Status;
import com.devlon.activitytracker.exception.CustomUserException;
import com.devlon.activitytracker.repository.TaskRepository;
import com.devlon.activitytracker.service.implementation.TaskServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
public class TaskController {
    private TaskServiceImpl taskService;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskServiceImpl taskService,
                          TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }


    @GetMapping("/task_form")
    public String taskForm(Model model) {
        TaskDTO task = new TaskDTO();
        model.addAttribute("task", task);
        return "activity-dashboard";

    }

    @GetMapping("/task")
    public String task(Model model, HttpSession httpSession) {
        TaskDTO task = (TaskDTO) httpSession.getAttribute("task");
        System.out.println(task);
        model.addAttribute("task", task);
        return "view-task";
    }
    @PostMapping("/create_task")
    public String createTask(@Valid @ModelAttribute("task")TaskDTO taskDTO, BindingResult bindingResult,
                             HttpSession httpSession) throws CustomUserException {

        if(bindingResult.hasErrors()) {
            return "activity-dashboard";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        taskService.saveTask(taskDTO, userDTO.getUserId());
        return "redirect:/all_task";
    }

    @GetMapping("/view_activity/{taskId}")
    public String viewTask(@ModelAttribute("task") TaskDTO taskDTO, Model model, @PathVariable Long taskId,
                           HttpSession httpSession) throws CustomUserException {
        if(taskId == null) {
            throw new CustomUserException("Invalid Id");
        }
        TaskDTO task = taskService.getTaskById(taskId);
        httpSession.setAttribute("task", task);
        return "redirect:/task";

    }

    @GetMapping("/all_task")
    public String allTask(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks =  taskService.getAllTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);
        return "redirect:/home";
    }
    @GetMapping("/task/pending")
    public String viewPendingTask(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getTasksByPendingStatus(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/home";
    }

    @GetMapping("/task/move_task/{taskId}")
    public String moveToProgress(HttpSession httpSession, @PathVariable Long taskId) throws CustomUserException, ExecutionException, InterruptedException {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
//        TaskDTO taskDTO = taskService.getTaskById(taskId);
//        if(taskDTO.getStatus() == Status.DONE){
//            throw new CustomUserException("Task has been completed and cannot be moved further. " +
//                    "You can consider moving it back");
//        }
//        else {
            taskService.moveTask(taskId);
//        }
        List<TaskDTO> tasks = taskService.getAllTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/all_task";
    }

    @GetMapping("/task/in_progress")
    public String viewTaskInProgress(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getTasksInProgressStatus(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/home";
    }

    @GetMapping("/task/completed")
    public String viewCompletedTask(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getAllCompletedTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/home";
    }
}
