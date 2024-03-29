package com.devlon.activitytracker.service.implementation;
import com.devlon.activitytracker.dto.LoginDTO;
import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.entity.User;
import com.devlon.activitytracker.exception.CustomUserException;
import com.devlon.activitytracker.repository.UserRepository;
import com.devlon.activitytracker.service.UserService;
//import me.iyanuadelekan.paystackjava.core.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
//import org.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

//        Transactions transactions = new Transactions();
//        transactions.initializeTransaction("[reference]","[amount]","[email]","[plan]","[callback_url]");

        userRepository.save(user);
    }

    @Override
    public UserDTO loginUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmailAndPassword(loginDTO.getUserName(), loginDTO.getPassword());
        if(user == null) {
           return null;
        }
        return mappedToDTO(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User getUserByPassword(String password) {
        return userRepository.findByPassword(password);
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
