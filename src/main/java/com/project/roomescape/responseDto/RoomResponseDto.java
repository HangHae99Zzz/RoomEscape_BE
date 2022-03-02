package com.project.roomescape.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponseDto {
    private String teamName;
    private Long count;
    private String createdUser;
    private Long currentNum;    // 현재 참여 인원

}
