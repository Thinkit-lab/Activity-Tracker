package com.devlon.activitytracker.repository;

import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.entity.Task;
import com.devlon.activitytracker.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@Testcontainers
//@ExtendWith(SpringExtension.class)
//@TestPropertySource(properties = {
//        "spring.jpa.hibernate.ddl-auto=validate"
//})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

//    @Container
//    static MySQLContainer database = new MySQLContainer()
//            .withDatabaseName("springboot")
//            .withPassword("springboot")
//            .withUsername("springboot");
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .userName("test")
                .email("test@gmail.com")
                .password("gurudesigner")
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
//        user.setTasks(tasks);

        entityManager.persist(user);

        Task task = Task.builder()
                .title("New task")
                .description("Task description")
                .completedAt(LocalDateTime.now())
                .user(user)
                .build();
        entityManager.persist(task);
    }

    @Test
    void contextLoad() {
        assertNotNull(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    @Disabled
    void findByEmailAndPassword() {
        User found = userRepository.findById(1L).get();
        assertEquals("test", found.getUserName());
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findByUserName() {
    }

    @Test
    void findByPassword() {
    }
}