package com.project.roomescape.controller;

import com.project.roomescape.responseDto.QuizResponseDto;
import com.project.roomescape.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuizController {

    public final QuizService quizService;

    // Quiz 조회하기
    @GetMapping("/escape/{roomId}/{quizType}")
    public QuizResponseDto getQuiz(@PathVariable Long roomId, @PathVariable String quizType) {
        return quizService.getQuiz(roomId, quizType);
    }

    // count +1
    @PostMapping("/escape/{roomId}")
    public void getCount(@PathVariable Long roomId) {
        quizService.getCount(roomId);
    }

}
