package com.devlon.activitytracker.repository;

import com.devlon.activitytracker.dto.LoginDTO;
import com.devlon.activitytracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from user s where s.user_name = ?1 and s.password = ?2",
            nativeQuery = true)
    User findByEmailAndPassword(String userName, String password);
}
