package com.project.roomescape.repository;

import com.project.roomescape.model.Clue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClueRepository extends JpaRepository<Clue, Long> {
    List<Clue> findAllByRoomId(Long roomId);
    void deleteClueByRoomId(Long roomId);
}
