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
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuizAbIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private RoomRepository roomRepository;

    @MockBean
    private GameResourceRepository mockGameResourceRepository;

    //방 개설하기부터 할 수 밖에 없습니다. 왜냐하면 퀴즈 생성할때 findbyRoomAndType이 있는데 이 로직은 무조건 룸이 먼저
    //저장되고 나서야 실행될 수 있는 로직이기 때문에 Room을 mock으로 처리할 수 없습니다(실제로 저장이 되야합니다).
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

        when(mockGameResourceRepository.findAllByType(gameResource1.getType())).thenReturn(mockGameResourceList);

        // when
        webTestClient.post().uri("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roomRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
//                .jsonPath("$.roomId").isEqualTo(1L)
                .jsonPath("$.teamName").isEqualTo("테스트팀")
                .jsonPath("$.createdUser").isEqualTo("테스트유저ID")
                .jsonPath("$.currentNum").isEqualTo(1)
//                .jsonPath("$.url").isEqualTo("/room/1")
                .jsonPath("$.userList").isNotEmpty()
                .jsonPath("$.startAt").isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("퀴즈 Ab 생성하기")
    void getQuizAb(){
        // when
        webTestClient.get().uri("/rooms/{roomId}/quizzes/{quizType}", 1, "Ab")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.question").isEqualTo("바이러스에 걸린 컴퓨터를 구할 숫자는?")
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.hint").isEmpty()
                .jsonPath("$.chance").isEqualTo("홀짝")
                .jsonPath("$.answer").isNotEmpty()
                .jsonPath("$.pass").isEqualTo("FAIL");

    }

    @Test
    @Order(3)
    @DisplayName("퀴즈 Ab 불러오기")
    void getSameQuizAb(){
        Optional<Room> room = roomRepository.findById(1L);
        Optional<Quiz> temporary = quizRepository.findByRoomAndType(room.get(), "Ab");
        QuizResponseDto quizResponseDto = new QuizResponseDto();
        if(temporary.isPresent()) {
            Quiz quiz = temporary.get();
            quizResponseDto = new QuizResponseDto(
                    quiz.getQuestion(), quiz.getContent(), quiz.getHint(),
                    quiz.getChance(), quiz.getAnswer(), quiz.getPass());
        }

        webTestClient.get().uri("/rooms/{roomId}/quizzes/{quizType}", 1, "Ab")
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

        webTestClient.put().uri("/rooms/{roomId}/quizzes/{quizType}", 1, "Ab")
                .exchange()
                .expectStatus().isOk();

        Optional<Room> room = roomRepository.findById(1L);
        Optional<Quiz> quiz = quizRepository.findByRoomAndType(room.get(),"Ab");

        assertEquals(quiz.get().getPass(), Pass.SUCCESS);

    }
}
