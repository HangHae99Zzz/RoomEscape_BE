package com.project.roomescape.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.roomescape.model.GameResource;
import com.project.roomescape.model.Room;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.GameResourceRepository;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import com.project.roomescape.service.RoomService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GameResourceRepository mockGameResourceRepository;

    @MockBean
    private RoomRepository mockRoomRepository;

    @Test
    @Order(1)
    @DisplayName("방 개설하기")
    void createRoom(){

        String teamName = "테스트팀";
        String userId = "테스트유저ID";
        RoomRequestDto roomRequestDto = new RoomRequestDto(teamName, userId);

        GameResource gameResource = new GameResource("userImg", "임시url");
        List<GameResource> mockGameResourceList = new ArrayList<>();
        mockGameResourceList.add(gameResource);
        mockGameResourceList.add(gameResource);
        mockGameResourceList.add(gameResource);
        mockGameResourceList.add(gameResource);

        when(mockGameResourceRepository.findAllByType(gameResource.getType())).thenReturn(mockGameResourceList);


        // when
        webTestClient.post().uri("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roomRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.roomId").isNotEmpty()
                .jsonPath("$.teamName").isEqualTo("테스트팀")
                .jsonPath("$.createdUser").isEqualTo("테스트유저ID")
                .jsonPath("$.currentNum").isEqualTo(1)
                .jsonPath("$.url").isNotEmpty()
                .jsonPath("$.userList").isNotEmpty()
                .jsonPath("$.startAt").isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("방 조회하기")
    void getRoom() {

    }




}
