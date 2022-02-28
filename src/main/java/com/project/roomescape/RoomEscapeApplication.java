package com.project.roomescape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@EnableJpaAuditing
@SpringBootApplication
public class RoomEscapeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }

}
