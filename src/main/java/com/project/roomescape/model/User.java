package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String img;

    public User(Room room, String nickName, String img) {
        this.room = room;
        this.nickName = nickName;
        this.img = img;
    }
}
