package com.devlon.activitytracker.controller;

import com.devlon.activitytracker.dto.LoginDTO;
import com.devlon.activitytracker.dto.TaskDTO;
import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.entity.Task;
import com.devlon.activitytracker.entity.User;
import com.devlon.activitytracker.service.implementation.TaskServiceImpl;
import com.devlon.activitytracker.service.implementation.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class UserController {

    private UserServiceImpl userService;
    private TaskServiceImpl taskService;

    @Autowired
    public UserController(UserServiceImpl userService, TaskServiceImpl taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String displayIndex(Model model) {
        model.addAttribute("user", new UserDTO());
        return "index";
    }

    @GetMapping("/home")
    public String displayHome(Model model, HttpSession httpSession) {
        List<TaskDTO> tasks = (List<TaskDTO>) httpSession.getAttribute("task");
        UserDTO currentUser = (UserDTO) httpSession.getAttribute("userDTO");
        model.addAttribute("user", new UserDTO());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("tasks", tasks);
        return "home";
    }
    @GetMapping("/success")
    public String registrationSuccess(Model model) {
        return "registration-success";
    }

    @GetMapping("/create-account")
    public String accountPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "index";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "index";
        }
        userService.registerUser(userDTO);
        return "redirect:/success";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("user") LoginDTO loginDTO, BindingResult bindingResult,
                            HttpSession httpSession) {
        if(bindingResult.hasErrors()) {
            return "index";
        }
        UserDTO userDTO = userService.loginUser(loginDTO);
        System.out.println(userDTO.getUserId());
        if(userDTO == null) {
            return "redirect:/";
        }
        List<TaskDTO> tasks =  taskService.getAllTask(userDTO.getUserId());
        httpSession.setAttribute("task", tasks);
        httpSession.setAttribute("userDTO", userDTO);
      return "redirect:/home";
    }
}
