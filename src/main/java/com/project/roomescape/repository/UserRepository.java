package com.project.roomescape.repository;

import com.project.roomescape.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    void deleteUserByUserId(String userId);
    Optional<User> findByUserId(String userId);

    List<User> findAllByRoomId(Long roomId);
}
