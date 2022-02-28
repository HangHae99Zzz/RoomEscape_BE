package com.project.roomescape.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Quiz extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private String description;

    public Quiz(String title, String content, String answer, String description) {
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.description = description;
    }

}
