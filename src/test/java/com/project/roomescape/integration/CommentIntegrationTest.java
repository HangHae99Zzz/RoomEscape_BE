package com.project.roomescape.integration;

import com.project.roomescape.model.Comment;
import com.project.roomescape.repository.CommentRepository;
import com.project.roomescape.requestDto.CommentRequestDto;
import com.project.roomescape.responseDto.CommentResponseDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.properties"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Order(1)
    @DisplayName("코멘트 입력하기")
    @Transactional
    void createComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto("임시 코멘트");

        webTestClient.post().uri("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentRequestDto)
                .exchange()
                .expectStatus().isOk();

        List<Comment> commentList = commentRepository.findAll();

        assertEquals(commentList.size(), 1);
        assertEquals(commentList.get(0).getComment(), "임시 코멘트");
    }

    @Test
    @Order(2)
    @DisplayName("코멘트 조회하기")
    @Transactional
    void getComments() {

        webTestClient.get().uri("/comments")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CommentResponseDto.class).hasSize(1).consumeWith(list -> {
                    assertEquals(list.getResponseBody().get(0).getComment(), "임시 코멘트");
                });
    }
}
