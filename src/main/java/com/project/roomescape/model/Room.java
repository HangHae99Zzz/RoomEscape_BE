package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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


}
