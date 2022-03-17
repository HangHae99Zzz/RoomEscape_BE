package com.project.roomescape.repository;

import com.project.roomescape.model.Quiz;
import com.project.roomescape.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByRoomAndType(Room room, String quiztype);
    void deleteQuizByRoomId(Long roomId);
}
