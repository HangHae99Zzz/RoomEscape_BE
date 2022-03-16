package com.project.roomescape.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RankRequestDto {
    private String time;

    public RankRequestDto(String time) {
        this.time = time;
    }

}