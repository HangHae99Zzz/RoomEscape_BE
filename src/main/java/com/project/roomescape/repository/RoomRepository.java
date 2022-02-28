package com.project.roomescape.repository;

import com.project.roomescape.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByTeamName(String teamName);
}
