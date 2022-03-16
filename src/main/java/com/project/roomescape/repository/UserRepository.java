package com.project.roomescape.repository;

import com.project.roomescape.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    void deleteUserByRoomId(Long roomId);
    List<User> findAllByRoomId(Long roomId);

}
