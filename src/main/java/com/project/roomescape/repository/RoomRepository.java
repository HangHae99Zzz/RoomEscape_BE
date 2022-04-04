package com.project.roomescape.repository;

import com.project.roomescape.model.Room;
import com.project.roomescape.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByState(Pageable pageable, State state);
}
