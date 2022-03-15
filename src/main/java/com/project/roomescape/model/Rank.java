package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ranks") // rank가 mySQL의 예약어 여서
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
    private Integer userNum;


    public Rank(String teamName, String time, Long roomId, Integer userNum) {
        this.teamName = teamName;
        this.time = time;
        this.roomId = roomId;
        this.userNum = userNum;
    }

//    public Rank(RankRequestDto rankRequestDto) {
//        this.time = rankRequestDto.getTime();
//        this.comment = rankRequestDto.getComment();
//        this.teamName = "";
//        this.roomId = rankRequestDto.getRoomId();
//        this.userNum = 1;
//    }
}
