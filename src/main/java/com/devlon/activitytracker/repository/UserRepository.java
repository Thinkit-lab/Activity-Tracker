package com.devlon.activitytracker.repository;

import com.devlon.activitytracker.dto.LoginDTO;
import com.devlon.activitytracker.dto.UserDTO;
import com.devlon.activitytracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from user s where s.user_name = ?1 and s.password = ?2",
            nativeQuery = true)
    User findByEmailAndPassword(String userName, String password);

    @Query(value = "select * from user s where s.email = ?1",
            nativeQuery = true)
    User findByEmail(String email);

    @Query(value = "select * from user s where s.user_name = ?1",
            nativeQuery = true)
    User findByUserName(String userName);

    @Query(value = "select * from user s where s.password = ?1",
            nativeQuery = true)
    User findByPassword(String password);
}
