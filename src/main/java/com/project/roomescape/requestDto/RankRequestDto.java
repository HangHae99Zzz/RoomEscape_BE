package com.project.roomescape.requestDto;

import lombok.Getter;

@Getter
public class RankRequestDto {
    private String time;
    private String comment;
    private Long roomId;

    public RankRequestDto(String time, String comment, Long roomId) {
        this.time = time;
        this.comment = comment;
        this.roomId = roomId;
    }

}