package com.project.roomescape.integration;

import com.project.roomescape.model.Rank;
import com.project.roomescape.repository.RankRepository;
import com.project.roomescape.responseDto.RankResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RankIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RankRepository rankRepository;

    @Test
    @Order(1)
    @DisplayName("전체랭킹 조회하기")
    void getRanks_SixRanks_GetSixRanks(){
        Rank rank1 = new Rank("임시1", "00:00:00", -1L, 0);
        Rank rank2 = new Rank("임시2", "00:00:00", -1L, 0);
        Rank rank3 = new Rank("임시3", "99:99:99", -1L, 0);
        Rank rank4 = new Rank("임시4", "99:99:99", -1L, 0);
        Rank rank5 = new Rank("진짜팀1", "03:00:00", 3L, 2);
        Rank rank6 = new Rank("진짜팀2", "01:00:00", 4L, 1);

        List<Rank> ranks = new ArrayList<>();
        ranks.add(rank1);
        ranks.add(rank2);
        ranks.add(rank3);
        ranks.add(rank4);
        ranks.add(rank5);
        ranks.add(rank6);

        //랭킹들을 먼저 저장합니다.
        rankRepository.saveAll(ranks);

        //실제 랭킹들이 제대로 잘 저장되었는지 get요청을 통해 확인합니다.
        webTestClient.get().uri("/ranks")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(RankResponseDto.class).hasSize(2).consumeWith(list -> {
                    assertEquals(list.getResponseBody().get(0).getRoomId(), 4L);
                    assertEquals(list.getResponseBody().get(0).getRank(), 1L);
                    assertEquals(list.getResponseBody().get(0).getTime(), "01:00:00");
                    assertEquals(list.getResponseBody().get(0).getTeamName(), "진짜팀2");
                    assertEquals(list.getResponseBody().get(0).getUserNum(), 1);

                    assertEquals(list.getResponseBody().get(1).getRoomId(), 3L);
                    assertEquals(list.getResponseBody().get(1).getRank(), 2L);
                    assertEquals(list.getResponseBody().get(1).getTime(), "03:00:00");
                    assertEquals(list.getResponseBody().get(1).getTeamName(), "진짜팀1");
                    assertEquals(list.getResponseBody().get(1).getUserNum(), 2);

                });

    }

    @Test
    @Order(2)
    @DisplayName("랭킹 5개 조회하기(00:00:00 두개 들어가는 경우)")
    void getRanks_FiveRanksAndTwoMocks_GetFiveRanksAndTwoMocks(){


        //get요청시 실제 랭킹 순서대로 잘 나오는지 확입합니다.
        webTestClient.get().uri("/ranks/{roomId}", 4)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(RankResponseDto.class).hasSize(5).consumeWith(list -> {

                    assertEquals(list.getResponseBody().get(0).getTime(), "00:00:00");

                    assertEquals(list.getResponseBody().get(1).getTime(), "00:00:00");

                    assertEquals(list.getResponseBody().get(2).getRoomId(), 4L);
                    assertEquals(list.getResponseBody().get(2).getRank(), 1L);
                    assertEquals(list.getResponseBody().get(2).getTime(), "01:00:00");
                    assertEquals(list.getResponseBody().get(2).getTeamName(), "진짜팀2");
                    assertEquals(list.getResponseBody().get(2).getUserNum(), 1);

                    assertEquals(list.getResponseBody().get(3).getRoomId(), 3L);
                    assertEquals(list.getResponseBody().get(3).getRank(), 2L);
                    assertEquals(list.getResponseBody().get(3).getTime(), "03:00:00");
                    assertEquals(list.getResponseBody().get(3).getTeamName(), "진짜팀1");
                    assertEquals(list.getResponseBody().get(3).getUserNum(), 2);

                    assertEquals(list.getResponseBody().get(4).getTime(), "99:99:99");
                });

    }

    @Test
    @Order(2)
    @DisplayName("랭킹 5개 조회하기(00:00:00 하나만 들어가는 경우)")
    void getRanks_FiveRanksAndOneMock_GetFiveRanksAndOneMock(){
        //새로운 랭크값을 DB에 넣어줌으로써 5개를 조회했을 때 00:00:00이 하나만 조회되게끔 합니다.
        Rank rank7 = new Rank("진짜팀3", "02:00:00", 5L, 2);
        rankRepository.save(rank7);

        //새로운 랭크를 중심으로 위아래 2개씩 조회합니다.
        webTestClient.get().uri("/ranks/{roomId}", 5)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(RankResponseDto.class).hasSize(5).consumeWith(list -> {

                    assertEquals(list.getResponseBody().get(0).getTime(), "00:00:00");

                    assertEquals(list.getResponseBody().get(1).getRoomId(), 4L);
                    assertEquals(list.getResponseBody().get(1).getRank(), 1L);
                    assertEquals(list.getResponseBody().get(1).getTime(), "01:00:00");
                    assertEquals(list.getResponseBody().get(1).getTeamName(), "진짜팀2");
                    assertEquals(list.getResponseBody().get(1).getUserNum(), 1);

                    assertEquals(list.getResponseBody().get(2).getRoomId(), 5L);
                    assertEquals(list.getResponseBody().get(2).getRank(), 2L);
                    assertEquals(list.getResponseBody().get(2).getTime(), "02:00:00");
                    assertEquals(list.getResponseBody().get(2).getTeamName(), "진짜팀3");
                    assertEquals(list.getResponseBody().get(2).getUserNum(), 2);

                    assertEquals(list.getResponseBody().get(3).getRoomId(), 3L);
                    assertEquals(list.getResponseBody().get(3).getRank(), 3L);
                    assertEquals(list.getResponseBody().get(3).getTime(), "03:00:00");
                    assertEquals(list.getResponseBody().get(3).getTeamName(), "진짜팀1");
                    assertEquals(list.getResponseBody().get(3).getUserNum(), 2);

                    assertEquals(list.getResponseBody().get(4).getTime(), "99:99:99");
                });

    }
}
