//package com.project.roomescape.integration;
//
//import com.project.roomescape.model.*;
//import com.project.roomescape.repository.*;
//import com.project.roomescape.requestDto.GameResourceRequestDto;
//import com.project.roomescape.requestDto.RankRequestDto;
//import com.project.roomescape.requestDto.RoomRequestDto;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class GameIntegrationTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Autowired
//    private GameResourceRepository gameResourceRepository;
//
//    @Autowired
//    private ClueRepository clueRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RankRepository rankRepository;
//
//
//    @Test
//    @Order(1)
//    @DisplayName("게임 리소스 3번 저장하기(나중에 방 개설할때 유저 이미지 때문입니다.)")
//    void saveGameResource(){
//        GameResourceRequestDto gameResourceRequestDto = new GameResourceRequestDto("userImg", "테스트url", "테스트타임");
//
//        for(int i = 0; i < 4; i++) {
//            webTestClient.post().uri("/games")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(gameResourceRequestDto)
//                    .exchange()
//                    .expectStatus().isOk();
//        }
//
//        Optional<GameResource> temp = gameResourceRepository.findById(1L);
//        GameResource gameResource = temp.get();
//
//        assertEquals(gameResource.getType(), "userImg");
//        assertEquals(gameResource.getUrl(), "테스트url");
//
//    }
//
//    //나중에 게임 종료시에 랭크를 저장합니다. 그때 room.getId()를 하는 부분이 있습니다. 근데 ID는 테스트에서 제가 임시로
//    //room을 만든다고 ID가 만들어지지 않습니다. 따라서 room을 직접 만드는 테스트가 포함되어야 합니다.
//    @Test
//    @Order(2)
//    @DisplayName("방 개설하기")
//    void createRoom(){
//
//        String teamName = "테스트팀";
//        String userId = "테스트유저ID";
//        RoomRequestDto roomRequestDto = new RoomRequestDto(teamName, userId);
//
//        // when
//        webTestClient.post().uri("/rooms")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(roomRequestDto)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.roomId").isEqualTo(5L)
//                .jsonPath("$.teamName").isEqualTo("테스트팀")
//                .jsonPath("$.createdUser").isEqualTo("테스트유저ID")
//                .jsonPath("$.currentNum").isEqualTo(1)
//                .jsonPath("$.url").isEqualTo("/room/5")
//                .jsonPath("$.userList").isNotEmpty()
//                .jsonPath("$.startAt").isEmpty();
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("게임 시작하기")
//    void startGame(){
//
//        webTestClient.put().uri("/games/{roomId}", 5)
//                .exchange()
//                .expectStatus().isOk();
//
//        List<Clue> clues = clueRepository.findAll();
//        Optional<Room> temp = roomRepository.findById(5L);
//        Room room = temp.get();
//
//        assertNotNull(room.getStartAt());
//        assertEquals(clues.size(), 3);
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("게임 종료하기")
//    void gameSuccessOver(){
//        RankRequestDto rankRequestDto = new RankRequestDto(true, "임시시간");
//
//        webTestClient.post().uri("/games/{roomId}", 5)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(rankRequestDto)
//                .exchange()
//                .expectStatus().isOk();
//
//        List<Clue> clues = clueRepository.findAll();
//        List<User> users = userRepository.findAll();
//        List<Rank> ranks = rankRepository.findAll();
//        Optional<Room> temp = roomRepository.findById(5L);
//        Room room = temp.get();
//
//        assertEquals(clues.size(), 0);
//        assertEquals(users.size(), 0);
//        assertEquals(ranks.size(), 1);
//        assertEquals(ranks.get(0).getTeamName(), "테스트팀");
//        assertEquals(ranks.get(0).getTime(), "임시시간");
//        assertEquals(ranks.get(0).getRoomId(), 5L);
//        assertEquals(ranks.get(0).getUserNum(), 1);
//        assertEquals(room.getPass(), Pass.SUCCESS);
//        assertEquals(room.getState(), State.CLOSE);
//        assertEquals(room.getUserNum(), 1);
//
//    }
//
//}