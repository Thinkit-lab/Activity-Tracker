package com.devlon.activitytracker.service;

import com.devlon.activitytracker.dto.LoginDTO;
import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.entity.User;

public interface UserService {
    void registerUser(UserDTO userDTO);

    UserDTO loginUser(LoginDTO loginDTO);

    User getUserByEmail(String email);

    User getUserByUsername(String userName);

    User getUserByPassword(String password);

}
