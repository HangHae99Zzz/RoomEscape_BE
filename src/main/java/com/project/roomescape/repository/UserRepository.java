package com.project.roomescape.repository;

import com.project.roomescape.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    void deleteUserById(Long id);
    void deleteUserByUserId(String userId);
    User findUserByUserId(String userId);
}
