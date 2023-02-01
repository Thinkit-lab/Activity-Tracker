package com.devlon.activitytracker.service;

import com.devlon.activitytracker.dto.LoginDTO;
import com.devlon.activitytracker.dto.UserDTO;

public interface UserService {
    void registerUser(UserDTO userDTO);

    UserDTO loginUser(LoginDTO loginDTO);
}
