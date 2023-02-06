package com.devlon.activitytracker.service.implementation;

import com.devlon.activitytracker.dto.LoginDTO;
import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.entity.User;
import com.devlon.activitytracker.exception.CustomUserException;
import com.devlon.activitytracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;
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

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByEmailAndPassword(user.getUserName(), user.getPassword()))
                .thenReturn(user);
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(user);
        Mockito.when(userRepository.findByPassword(user.getPassword())).thenReturn(user);
        Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

    }

    @Test
    @DisplayName("Save User to Database")
    void registerUserTest() {
        UserDTO user = UserDTO.builder()
                .userName("Olu")
                .email("Olu@gmail.com")
                .password("olumide")
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userService.registerUser(user);
        assertEquals("Olu", user.getUserName());
    }

    @Test
    @DisplayName("User Login test")
    void loginUserTest() {
        LoginDTO loginDTO = LoginDTO.builder()
                .userName("Olu")
                .email("Olu@gmail.com")
                .password("olumide")
                .build();
        loginDTO.setCreatedAt(LocalDateTime.now());
        loginDTO.setUpdatedAt(LocalDateTime.now());
        UserDTO user = userService.loginUser(loginDTO);

        assertEquals("Olu", user.getUserName());
    }

    @Test
    @DisplayName("Get user by email")
    void getUserByEmail() {
        LoginDTO loginDTO = LoginDTO.builder()
                .userName("Olu")
                .email("Olu@gmail.com")
                .password("olumide")
                .build();
        loginDTO.setCreatedAt(LocalDateTime.now());
        loginDTO.setUpdatedAt(LocalDateTime.now());

        User user = userService.getUserByEmail(loginDTO.getEmail());

        assertNotNull(user);
    }

    @Test
    @DisplayName("Get user by username")
    void getUserByUsername() {
        LoginDTO loginDTO = LoginDTO.builder()
                .userName("Olu")
                .email("Olu@gmail.com")
                .password("olumide")
                .build();
        loginDTO.setCreatedAt(LocalDateTime.now());
        loginDTO.setUpdatedAt(LocalDateTime.now());

        User user = userService.getUserByUsername("Olu");
        assertNotNull(user);
    }

    @Test
    @DisplayName("Get user by password")
    void getUserByPassword() {
        User user = userService.getUserByPassword("olumide");
        assertNotNull(user);
    }

    @Test
    @DisplayName("Get user by Id")
    void getUserById() throws CustomUserException {
        UserDTO user = userService.getUserById(1L);

        assertNotNull(user);
    }
}