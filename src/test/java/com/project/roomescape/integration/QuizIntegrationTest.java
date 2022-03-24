package com.project.roomescape.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.roomescape.responseDto.QuizResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuizIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;
    // 객체를 Json으로 serialization하거나 Json을 객체로 deserialization한다.
    private ObjectMapper mapper = new ObjectMapper();



    @Test
    @Order(1)
    @DisplayName("Quiz 조회하기")
    void getQuiz() throws JsonProcessingException {

        ResponseEntity<QuizResponseDto> response = restTemplate.getForEntity(
                "/rooms/" + roomId + "/quizzes/" + quizType,
                QuizResponseDto.class);

    }
}

