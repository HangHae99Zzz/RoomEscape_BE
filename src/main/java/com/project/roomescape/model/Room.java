package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Room extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private Long count;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String createdUser;

    @Column(nullable = false)
    private Long clueA;

    @Column(nullable = false)
    private Long clueB;

    @OneToMany(mappedBy = "room")
    private List<User> userList = new ArrayList<>();


    public Room(String teamName, String createdUser, Long clueA, Long clueB) {
        this.count = 0L;
        this.teamName = teamName;
        this.createdUser = createdUser;
        this.clueA = clueA;
        this.clueB = clueB;
    }


}
