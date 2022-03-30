package com.project.roomescape.integration.quizintegrationtest;

import com.project.roomescape.model.GameResource;
import com.project.roomescape.model.Pass;
import com.project.roomescape.model.Quiz;
import com.project.roomescape.model.Room;
import com.project.roomescape.repository.GameResourceRepository;
import com.project.roomescape.repository.QuizRepository;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.QuizResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


//통합테스트를 실시합니다. 포트는 랜덤 포트로 실행합니다. properties도 기존의 application.properties가 아닌 application-test.properties로 합니다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
//테스트 인스턴스의 생명주기를 클래스 단위로 합니다.(테스트 실행범위라고 생각)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@order 순서에 따라서 테스트를 진행합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuizAaIntegrationTest {

    //Autowired를 통해서 의존성 주입을 실시합니다.
    //제가 @RequiredArgsConstructor 방식으로 의존성 주입을 안한 이유입니다.
    //(difference in autowire handling between Spring and Spring integration with JUnit)
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private RoomRepository roomRepository;

    //실제 GameResourceRepository에 있는 파일들을 불러올 수 없기 때문에 @MockBean으로 설정함.
    @MockBean
    private GameResourceRepository mockGameResourceRepository;

    //방 개설하기부터 할 수 밖에 없습니다. 왜냐하면 퀴즈 생성할때 findbyRoomAndType이 있는데 이 로직은 무조건 룸이 먼저
    //저장되고 나서야 실행될 수 있는 로직이기 때문에 Room을 mock으로 처리할 수 없습니다.
    @Test
    @Order(1)
    @DisplayName("방 개설하기")
    void createRoom(){

        String teamName = "테스트팀";
        String userId = "테스트유저ID";
        RoomRequestDto roomRequestDto = new RoomRequestDto(teamName, userId);

        GameResource gameResource1 = new GameResource("userImg", "임시url1");
        GameResource gameResource2 = new GameResource("userImg", "임시url2");
        GameResource gameResource3 = new GameResource("userImg", "임시url3");
        GameResource gameResource4 = new GameResource("userImg", "임시url4");
        List<GameResource> mockGameResourceList = new ArrayList<>();
        mockGameResourceList.add(gameResource1);
        mockGameResourceList.add(gameResource2);
        mockGameResourceList.add(gameResource3);
        mockGameResourceList.add(gameResource4);

        //MockBean 호출시 만들어 놓은 mockGameResourceList를 return해줍니다.
        when(mockGameResourceRepository.findAllByType(gameResource1.getType())).thenReturn(mockGameResourceList);

        // when
        webTestClient.post().uri("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roomRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.roomId").isEqualTo(1L)
                .jsonPath("$.teamName").isEqualTo("테스트팀")
                .jsonPath("$.createdUser").isEqualTo("테스트유저ID")
                .jsonPath("$.currentNum").isEqualTo(1)
                .jsonPath("$.url").isEqualTo("/room/1")
                .jsonPath("$.userList").isNotEmpty()
                .jsonPath("$.startAt").isEmpty();
    }


    @Test
    @Order(2)
    @DisplayName("퀴즈 Aa 생성하기")
    void getQuizAa(){
        // when
        webTestClient.get().uri("/rooms/{roomId}/quizzes/{quizType}", 1, "Aa")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.question").isEqualTo("지금 몇시지?")
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.hint").isEmpty()
                .jsonPath("$.chance").isEqualTo("시계를 돌려볼까?")
                .jsonPath("$.answer").isNotEmpty()
                .jsonPath("$.pass").isEqualTo("FAIL");

    }

    @Test
    @Order(3)
    @DisplayName("퀴즈 Aa 불러오기")
    void getSameQuizAa(){
        Optional<Room> room = roomRepository.findById(1L);
        Optional<Quiz> temporary = quizRepository.findByRoomAndType(room.get(), "Aa");
        QuizResponseDto quizResponseDto = new QuizResponseDto();
        if(temporary.isPresent()) {
            Quiz quiz = temporary.get();
            quizResponseDto = new QuizResponseDto(
                    quiz.getQuestion(), quiz.getContent(), quiz.getHint(),
                    quiz.getChance(), quiz.getAnswer(), quiz.getPass());
        }

        webTestClient.get().uri("/rooms/{roomId}/quizzes/{quizType}", 1, "Aa")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.question").isEqualTo(quizResponseDto.getQuestion())
                .jsonPath("$.content").isEqualTo(quizResponseDto.getContent())
                .jsonPath("$.hint").isEqualTo(quizResponseDto.getHint())
                .jsonPath("$.chance").isEqualTo(quizResponseDto.getChance())
                .jsonPath("$.answer").isEqualTo(quizResponseDto.getAnswer())
                .jsonPath("$.pass").isEqualTo("FAIL");
    }

    @Test
    @Order(4)
    @DisplayName("퀴즈 완료시키기")
    void finishedQuiz() {

        webTestClient.put().uri("/rooms/{roomId}/quizzes/{quizType}", 1, "Aa")
                .exchange()
                .expectStatus().isOk();

        Optional<Room> room = roomRepository.findById(1L);
        Optional<Quiz> quiz = quizRepository.findByRoomAndType(room.get(),"Aa");

        assertEquals(quiz.get().getPass(), Pass.SUCCESS);

    }


}
