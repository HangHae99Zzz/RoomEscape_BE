package com.project.roomescape.service;

import com.project.roomescape.model.Room;
import com.project.roomescape.repository.ClueRepository;
import com.project.roomescape.repository.QuizRepository;
import com.project.roomescape.responseDto.QuizResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTimeTest {

    @Spy
    ClueRepository mockClueRepository;

    @Spy
    QuizRepository mockQuizRepository;

    @InjectMocks
    private QuizService quizService;

    @Test
    @DisplayName("퀴즈 Aa생성 시간 테스트")
    void createQuiz_QuizTypeAa_CreateQuizAa() {
        long beforeTime = System.currentTimeMillis();
        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizAa(room, "Aa");

        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산(초로 변환)
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());
    }

    @Test
    @DisplayName("퀴즈 Ab생성 시간 테스트")
    void createQuiz_QuizTypeAb_CreateQuizAb() {
        long beforeTime = System.currentTimeMillis();
        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizAb(room, "Ab");

        long afterTime = System.currentTimeMillis();
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());
    }

    @Test
    @DisplayName("퀴즈 Ba생성 시간 테스트 & 정답확인")
    void createQuiz_QuizTypeBa_CreateQuizBa() {
        long beforeTime = System.currentTimeMillis();
        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizBa(room, "Ba");

        long afterTime = System.currentTimeMillis();
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());
    }

    @Test
    @DisplayName("퀴즈 Bb생성 시간 테스트")
    void createQuiz_QuizTypeBb_CreateQuizBb() {
        long beforeTime = System.currentTimeMillis();
        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizBb(room, "Bb");

        long afterTime = System.currentTimeMillis();
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());
    }

    @Test
    @DisplayName("퀴즈 Ca생성 시간 테스트")
    void createQuiz_QuizTypeCa_CreateQuizCa() {
        long beforeTime = System.currentTimeMillis();

        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizCa(room, "Ca");

        long afterTime = System.currentTimeMillis();
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());
    }
}