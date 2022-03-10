package com.project.roomescape.requestDto;

import lombok.Getter;

@Getter
public class RankRequestDto {
    private String time;
    private String comment;

    public RankRequestDto(String time, String comment) {
        this.time = time;
        this.comment = comment;
    }


}