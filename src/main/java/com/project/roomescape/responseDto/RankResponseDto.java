package com.project.roomescape.responseDto;

import lombok.Getter;

@Getter
public class RankResponseDto {
    private Long roomId;
    private Long rank;
    private String teamName;
    private String time;
    private Integer userNum;


    public RankResponseDto(Long roomId, Long rank, String teamName, String time, Integer userNum) {
        this.roomId = roomId;
        this.rank = rank;
        this.teamName = teamName;
        this.time = time;
        this.userNum = userNum;
    }



}
