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

    //mock과 spy들을 quizService에 주입시킨다
    @InjectMocks
    private QuizService quizService;

    @Test
    @DisplayName("퀴즈 Aa생성 시간 테스트")
    void createQuiz_QuizTypeAa_CreateQuizAa() {

        //코드 실행 전에 시간 받아오기 (밀리세컨드)
        long beforeTime = System.currentTimeMillis();

        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizAa(room, "Aa");

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산(초로 변환)
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());

    }

    @Test
    @DisplayName("퀴즈 Ab생성 시간 테스트")
    void createQuiz_QuizTypeAb_CreateQuizAb() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();

        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizAb(room, "Ab");

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());
    }

    @Test
    @DisplayName("퀴즈 Ba생성 시간 테스트 & 정답확인")
    void createQuiz_QuizTypeBa_CreateQuizBa() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();

        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizBa(room, "Ba");

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());

    }

    @Test
    @DisplayName("퀴즈 Bb생성 시간 테스트")
    void createQuiz_QuizTypeBb_CreateQuizBb() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();

        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizBb(room, "Bb");

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());
    }

    @Test
    @DisplayName("퀴즈 Ca생성 시간 테스트")
    void createQuiz_QuizTypeCa_CreateQuizCa() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();

        Room room = new Room("임시팀", "임시유저");

        QuizResponseDto quizResponseDto = quizService.createQuizCa(room, "Ca");

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
        assertNotNull(quizResponseDto.getAnswer());
    }
}
