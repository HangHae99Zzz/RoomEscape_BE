package com.project.roomescape.responseDto;

import lombok.Getter;

@Getter
public class RankResponseDto {
    private String teamName;
    private String time;

    public RankResponseDto(String teamName, String time) {
        this.teamName = teamName;
        this.time = time;
    }
}
