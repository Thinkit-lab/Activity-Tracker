package com.devlon.activitytracker.service;

import com.devlon.activitytracker.entity.Task;
import com.devlon.activitytracker.entity.User;
import com.devlon.activitytracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUserName(),
                    user.getPassword(),
                    getAuthorities(user.getTasks()));
        }else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<Task> tasks) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Task task : tasks) {
            authorities.add(new SimpleGrantedAuthority(task.getTitle()));
        }
        return authorities;
    }
}