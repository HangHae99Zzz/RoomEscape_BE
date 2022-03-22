package com.project.roomescape.controller;

import com.project.roomescape.responseDto.QuizResponseDto;
import com.project.roomescape.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class QuizController {

    public final QuizService quizService;

    // Quiz 조회하기
    @GetMapping("/rooms/{roomId}/quizzes/{quizType}")
    public QuizResponseDto getQuiz(@PathVariable Long roomId, @PathVariable String quizType) {
        return quizService.getQuiz(roomId, quizType);
    }

    // Quiz 완료시
    @PutMapping("/rooms/{roomId}/quizzes/{quizType}")
    public void finishedQuiz(@PathVariable Long roomId, @PathVariable String quizType) {
        quizService.finishedQuiz(roomId, quizType);
    }

}
