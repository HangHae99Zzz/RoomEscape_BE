package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
// rank가 mySQL의 예약어
@Table(name = "ranks")
public class Rank extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private int userNum;

    public Rank(String teamName, String time, Long roomId, int userNum) {
        this.teamName = teamName;
        this.time = time;
        this.roomId = roomId;
        this.userNum = userNum;
    }

}
