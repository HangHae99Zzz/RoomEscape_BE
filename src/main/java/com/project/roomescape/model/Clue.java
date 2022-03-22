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

    private Long roomId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String content;

    public Clue(Long roomId, String type, String content) {
        this.roomId = roomId;
        this.type = type;
        this.content = content;
    }
}

