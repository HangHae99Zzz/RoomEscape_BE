package com.project.roomescape.integration;

import com.project.roomescape.model.GameResource;
import com.project.roomescape.model.Room;
import com.project.roomescape.repository.GameResourceRepository;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RoomRepository roomRepository;


    @MockBean
    private GameResourceRepository mockGameResourceRepository;


    @Test
    @Order(1)
    @DisplayName("방 개설하기")
    void createRoom_OneRoom_CreateOneRoom(){

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
    @DisplayName("방 조회하기")
    @Transactional
    void getRoom_OneRoom_GetOneRoom() {
        //실제로 room을 DB에서 가져옵니다.
        Optional<Room> temp = roomRepository.findById(1L);
        Room room = temp.get();


        //실제 get요청시 response 값과 db에서 찾아온 room정보가 일치하는지 확인합니다.
        webTestClient.get().uri("/rooms/{roomId}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.roomId").isEqualTo(room.getId())
                .jsonPath("$.teamName").isEqualTo(room.getTeamName())
                .jsonPath("$.createdUser").isEqualTo(room.getCreatedUser())
                .jsonPath("$.currentNum").isEqualTo(1)
                .jsonPath("$.url").isEqualTo("/room/1")
                .jsonPath("$.userList").isNotEmpty()
                .jsonPath("$.startAt").isEmpty();

    }

    @Test
    @Order(3)
    @DisplayName("방 참여하기")
    @Transactional
    void joinRoom_OneRoom_JoinOneRoom() {

        String userId = "테스트참가유저ID";
        RoomAddRequestDto roomAddRequestDto = new RoomAddRequestDto(userId);

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

        webTestClient.post().uri("/rooms/{roomId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roomAddRequestDto)
                .exchange()
                .expectStatus().isOk();

        Optional<Room> temp = roomRepository.findById(1L);
        Room room = temp.get();

        //collection(userList)은 기본적으로 lazy-loaded이기 때문에 @Transactional을 붙여준다.
        //실제로 방 참여로 room의 size가 2가 되었는지 체크합니다. 그리고 참가한 유저가 userlist에 제대로 들어갔는지 확인합니다.
        assertEquals(room.getUserList().size(), 2);
        assertEquals(room.getUserList().get(1).getUserId(), "테스트참가유저ID");



    }

    @Test
    @Order(4)
    @DisplayName("방 리스트 조회하기")
    void getRooms_AllRooms_GetAllRooms() {

        //get요청시 실제로 roomlist가 제대로 나오는지 체크합니다.
        webTestClient.get().uri("/rooms/pages/{page}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(RoomResponseDto.class).hasSize(1).consumeWith(list -> {
                    assertEquals(list.getResponseBody().get(0).getRoomId(), 1L);
                    assertEquals(list.getResponseBody().get(0).getCreatedUser(), "테스트유저ID");
                    assertEquals(list.getResponseBody().get(0).getTeamName(), "테스트팀");
                    assertEquals(list.getResponseBody().get(0).getUrl(), "/room/1");
                    assertEquals(list.getResponseBody().get(0).getUserList().size(), 2);
                    assertEquals(list.getResponseBody().get(0).getCurrentNum(), 2);
                    assertNull(list.getResponseBody().get(0).getStartAt());
                });
    }
}
