package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Room extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roomId")
    @Id
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String createdUser;

    @OneToMany(mappedBy = "room")
    private List<User> userList = new ArrayList<>();

    @Column(nullable = true)
    private Long startAt;

    @Column(nullable = true)
    private Pass pass;

    @Column(nullable = false)
    private State state;

    @Column(nullable = true)
    private Long userNum;

    public Room(String teamName, String createdUser) {
        this.teamName = teamName;
        this.createdUser = createdUser;
        this.startAt = null;
        this.pass = null;
        this.state = State.ACTIVE;
        this.userNum = null;
    }

    public void gameOver(Pass pass, Long userNum) {
        this.pass = pass;
        this.state = State.CLOSE;
        this.userNum = userNum;
    }

    public void setStartAt(Long startAt) {
        this.startAt = startAt;
    }

}
