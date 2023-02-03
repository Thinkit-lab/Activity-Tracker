package com.devlon.activitytracker.controller;
import com.devlon.activitytracker.dto.TaskDTO;
import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.exception.CustomUserException;
import com.devlon.activitytracker.service.implementation.TaskServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
public class TaskController {
    private final TaskServiceImpl taskService;

    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
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
    public String viewTask(@ModelAttribute("task") TaskDTO taskDTO, @PathVariable Long taskId,
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
        return "redirect:/listTasks";
    }
    @GetMapping("/task/pending")
    public String viewPendingTask(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getTasksByPendingStatus(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/listTasks";
    }

    @GetMapping("/task/move_task/{taskId}")
    public String moveTask(HttpSession httpSession, @PathVariable Long taskId) throws CustomUserException, ExecutionException, InterruptedException {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");

        taskService.moveTask(taskId);

        List<TaskDTO> tasks = taskService.getAllTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/all_task";
    }

    @GetMapping("/task/move_back/{taskId}")
    public String moveBack(HttpSession httpSession, @PathVariable Long taskId) throws CustomUserException, ExecutionException, InterruptedException {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");

        taskService.moveBack(taskId);

        List<TaskDTO> tasks = taskService.getAllTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/all_task";
    }

    @GetMapping("/task/delete/{taskId}")
    public String deleteTask(HttpSession httpSession, @PathVariable Long taskId) throws CustomUserException, ExecutionException, InterruptedException {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");

        taskService.deleteTask(taskId);

        List<TaskDTO> tasks = taskService.getAllTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/all_task";
    }

    @GetMapping("/task/in_progress")
    public String viewTaskInProgress(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getTasksInProgressStatus(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/listTasks";
    }

    @GetMapping("/task/completed")
    public String viewCompletedTask(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getAllCompletedTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/listTasks";
    }

    @GetMapping("/edit_task_form/{taskId}")
    public String editTaskForm(Model model, @PathVariable Long taskId) throws CustomUserException {
        TaskDTO taskDTO = new TaskDTO();
        model.addAttribute("task", taskDTO);
        model.addAttribute("currentTaskId", taskId);
        return "edit-activity";
    }

    @PostMapping("/edit_task/{taskId}")
    public String editTask(@Valid @ModelAttribute("task")TaskDTO taskDTO, BindingResult bindingResult,
                             HttpSession httpSession, @PathVariable Long taskId) throws CustomUserException {

        if(bindingResult.hasErrors()) {
            return "edit-activity";
        }
        taskService.editTask(taskDTO, taskId);

        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getAllTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/all_task";
    }

    @GetMapping("/go_back")
    public String goBack() {
        return "redirect:/all_task";
    }

    @GetMapping("/search")
    public String searchTask(@Valid @ModelAttribute("task")TaskDTO taskDTO, BindingResult bindingResult,
                             HttpSession httpSession) {
        System.out.println(taskDTO.getTitle());
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        System.out.println(userDTO.getUserId());

        List<TaskDTO> tasks = taskService.searchTask(taskDTO.getTitle(), userDTO.getUserId());
        log.info("Printing task");
        httpSession.setAttribute("task", tasks);
        return "redirect:/listTasks";
    }

    @GetMapping("/listTasks")
    public String listTasks(
            Model model, HttpSession httpSession,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        List<TaskDTO> tasks = (List<TaskDTO>) httpSession.getAttribute("task");
        TaskDTO task = new TaskDTO();

        Page<TaskDTO> taskPage = taskService.findPaginated(tasks, PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("taskPage", taskPage);

        int totalPages = taskPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        UserDTO currentUser = (UserDTO) httpSession.getAttribute("userDTO");
        model.addAttribute("task", task);
        model.addAttribute("user", new UserDTO());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("tasks", tasks);

        return "home";
    }
}
