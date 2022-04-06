package com.project.roomescape.integration;

import com.project.roomescape.model.Clue;
import com.project.roomescape.model.Room;
import com.project.roomescape.repository.ClueRepository;
import com.project.roomescape.repository.RoomRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClueIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ClueRepository clueRepository;

    @MockBean
    private RoomRepository mockRoomRepository;

    //게임시작해야 clue가 만들어지기 때문에 게임시작하기부터 실행한다.
    @Test
    @Order(1)
    @DisplayName("게임시작하기")
    void startGame_OneRoom_StartGameAndCreateClue(){
        Room room = new Room("테스트팀", "테스트유저ID");

        when(mockRoomRepository.findById(1L)).thenReturn(Optional.of(room));


        webTestClient.put().uri("/games/{roomId}", 1)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(2)
    @DisplayName("Ba1 clue 조회하기")
    void getClue_ClueTypeBa1_GetClueBa1(){
        //만든 clue를 찾아옵니다.
        Clue clue = clueRepository.findByRoomIdAndType(1L, "Ba1");

        //get요청으로 받은 response와 실제 찾아온 clue가 같은지 비교합니다.
        webTestClient.get().uri("/rooms/{roomId}/clues/{clueType}", 1, "Ba1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.content").isEqualTo(clue.getContent());
    }

    @Test
    @Order(3)
    @DisplayName("Ba2 clue 조회하기")
    void getClue_ClueTypeBa2_GetClueBa2(){
        Clue clue = clueRepository.findByRoomIdAndType(1L, "Ba2");

        webTestClient.get().uri("/rooms/{roomId}/clues/{clueType}", 1, "Ba2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.content").isEqualTo(clue.getContent());
    }

    @Test
    @Order(4)
    @DisplayName("Ba3 clue 조회하기")
    void getClue_ClueTypeBa3_GetClueBa3(){
        Clue clue = clueRepository.findByRoomIdAndType(1L, "Ba3");

        webTestClient.get().uri("/rooms/{roomId}/clues/{clueType}", 1, "Ba3")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.content").isEqualTo(clue.getContent());
    }

    @Test
    @Order(5)
    @DisplayName("DB에 존재하지 않는 clue 조회하기")
    void getClue_ClueTypeAA_GetClueAA(){

        //DB에 존재하지 않는 타입인 AA 유형을 요청함으로써 제대로 응답이 오는지 확인.
        webTestClient.get().uri("/rooms/{roomId}/clues/{clueType}", 1, "AA")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.content").isEqualTo("");
    }

}
