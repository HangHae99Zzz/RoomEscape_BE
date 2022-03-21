package com.project.roomescape.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RankResponseDto {
    private Long roomId;
    private Long rank;
    private String teamName;
    private String time;
    private int userNum;

}
