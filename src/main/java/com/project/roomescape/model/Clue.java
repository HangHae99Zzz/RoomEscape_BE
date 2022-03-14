package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
public class Clue {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String content;

    public Clue(Room room, String type, String content) {
        this.room = room;
        this.type = type;
        this.content = content;
    }
}

