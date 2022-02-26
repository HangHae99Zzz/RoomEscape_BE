package com.project.roomescape.repository;

import com.project.roomescape.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
