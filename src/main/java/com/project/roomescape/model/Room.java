package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Room extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "quizId")
    private Quiz quiz;

    @Column(nullable = false)
    private Long count;

    @Column(nullable = false)
    private String teamName;



    public Long getId() {
        return id;
    }

    public Room(Quiz quiz, Long count, String teamName) {
        this.quiz = quiz;
        this.count = count;
        this.teamName = teamName;
    }

    public Map<String, WebSocketSession> getClients() {
        Map<String, WebSocketSession> clients = new HashMap<>();
        return clients;
    }


}
