package com.devlon.activitytracker.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {
            "/home",
            "/index",
            "/",
            "/login",
            "/logout",
            "/success",
            "/create-account",
            "/register",
            "/edit-activity",
            "/edit_task_form/{taskId}",
            "/task/completed",
            "/task/in_progress",
            "/task/delete/{taskId}",
            "/task/move_back/{taskId}",
            "/task/move_task/{taskId}",
            "/task/pending",
            "/all_task",
            "/view_activity/{taskId}",
            "/create_task",
            "/task",
            "/task_form",
            "/go_back",
            "/edit_task/{taskId}",
            "edit-activity",
            "/listTasks",
            "/signup",
            "/success",
            "/search",


            "/registration-success",
            "/view-task",
            "/style.css",
            "/index.js",
            "/images/**"
    };

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .and()
                .formLogin(form-> form.loginPage("/")
                        .defaultSuccessUrl("/home")
                        .loginProcessingUrl("/")
                        .failureUrl("/?error=true")
                        .permitAll()
                ).logout(
                        logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                );

        return httpSecurity.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService);
//                .passwordEncoder(passwordEncoder());
    }
}
