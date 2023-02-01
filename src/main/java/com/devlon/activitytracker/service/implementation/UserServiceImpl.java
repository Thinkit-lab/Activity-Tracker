package com.devlon.activitytracker.service.implementation;

import com.devlon.activitytracker.dto.LoginDTO;
import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.entity.User;
import com.devlon.activitytracker.exception.CustomUserException;
import com.devlon.activitytracker.repository.UserRepository;
import com.devlon.activitytracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = User.builder()
                .userName(userDTO.getUserName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword()).build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(user.getUpdatedAt());

        userRepository.save(user);
    }

    @Override
    public UserDTO loginUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmailAndPassword(loginDTO.getUserName(), loginDTO.getPassword());

        return mappedToDTO(user);
    }

    public UserDTO getUserById(Long userId) throws CustomUserException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return mappedToDTO(user.get());
        }
        throw new CustomUserException("User not found");
    }

    private UserDTO mappedToDTO(User user) {
        UserDTO userDTO = UserDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setUserId(user.getUserId());

        return userDTO;
    }
}
