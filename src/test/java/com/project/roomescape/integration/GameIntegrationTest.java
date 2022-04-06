package com.project.roomescape.integration;

import com.project.roomescape.model.*;
import com.project.roomescape.repository.*;
import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.requestDto.RankRequestDto;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.repository.*;
import com.project.roomescape.service.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private GameResourceRepository gameResourceRepository;

    @Autowired
    private ClueRepository clueRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankRepository rankRepository;


    @Test
    @Order(1)
    //나중에 방 개설할때 유저 이미지가 필요하기 때문에 게임 리소스부터 여러번 저장합니다.
    @DisplayName("게임 리소스 여러번 저장하기")
    void createGameResource_OneGameResource_CreateOneGameResource(){
        GameResourceRequestDto gameResourceRequestDto = new GameResourceRequestDto("userImg", "테스트url");

        //반복문으로 여러번 게임 리소스를 저장합니다.
        for(int i = 0; i < 4; i++) {
            webTestClient.post().uri("/games/resources")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(gameResourceRequestDto)
                    .exchange()
                    .expectStatus().isOk();
        }
        //게임 리소스를 찾아옵니다.
        Optional<GameResource> temp = gameResourceRepository.findById(1L);
        GameResource gameResource = temp.get();
        //게임 리소스가 제대로 들어갔는지 확인합니다.
        assertEquals(gameResource.getType(), "userImg");
        assertEquals(gameResource.getUrl(), "테스트url");

    }

    //나중에 게임 종료시에 랭크를 저장합니다. 그때 room.getId()를 하는 로직이 있습니다. 근데 ID는 테스트에서 제가 임시로
    //room을 만든다고 ID가 만들어지지 않습니다. 따라서 room을 직접 만들고 DB에 저장하는 테스트가 포함되어야 합니다.
    @Test
    @Order(2)
    @DisplayName("방 개설하기")
    void createRoom_OneRoom_CreateOneRoom(){

        String teamName = "테스트팀";
        String userId = "테스트유저ID";
        RoomRequestDto roomRequestDto = new RoomRequestDto(teamName, userId);

        // when
        webTestClient.post().uri("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roomRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.roomId").isEqualTo(5L)
                .jsonPath("$.teamName").isEqualTo("테스트팀")
                .jsonPath("$.createdUser").isEqualTo("테스트유저ID")
                .jsonPath("$.currentNum").isEqualTo(1)
                .jsonPath("$.url").isEqualTo("/room/5")
                .jsonPath("$.userList").isNotEmpty()
                .jsonPath("$.startAt").isEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("게임 시작하기")
    void startGame_OneRoom_StartGameAndCreateClue(){

        webTestClient.put().uri("/games/{roomId}", 5)
                .exchange()
                .expectStatus().isOk();

        //게임 시작하고 난 후에 clue를 찾아옵니다.
        List<Clue> clues = clueRepository.findAll();
        Optional<Room> temp = roomRepository.findById(5L);
        Room room = temp.get();

        //실제로 제대로 clue와 room이 저장되었는지 체크합니다.
        assertNotNull(room.getStartAt());
        assertEquals(clues.size(), 3);
    }

    @Test
    @Order(4)
    @DisplayName("게임 종료하기")
    void endGame_CreateRank_endGameAndCreateRank(){
        RankRequestDto rankRequestDto = new RankRequestDto(true, "임시시간");

        webTestClient.post().uri("/games/{roomId}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(rankRequestDto)
                .exchange()
                .expectStatus().isOk();

        //게임 종료후에 clue, user, rank, room을 찾아옵니다.
        List<Clue> clues = clueRepository.findAll();
        List<User> users = userRepository.findAll();
        List<Rank> ranks = rankRepository.findAll();
        Optional<Room> temp = roomRepository.findById(5L);
        Room room = temp.get();


        //실제로 clue와 user들이 삭제 되었는지와 rank, room이 제대로 저장되었는지 체크합니다.
        assertEquals(clues.size(), 0);
        assertEquals(users.size(), 0);
        assertEquals(ranks.size(), 1);
        assertEquals(ranks.get(0).getTeamName(), "테스트팀");
        assertEquals(ranks.get(0).getTime(), "임시시간");
        assertEquals(ranks.get(0).getRoomId(), 5L);
        assertEquals(ranks.get(0).getUserNum(), 1);
        assertEquals(room.getPass(), Pass.SUCCESS);
        assertEquals(room.getState(), State.CLOSE);
        assertEquals(room.getUserNum(), 1);

    }

}
