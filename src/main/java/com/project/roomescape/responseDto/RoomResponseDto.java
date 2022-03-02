package com.project.roomescape.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponseDto {
    private Long roomId;
    private String teamName;
    private Long count;
    private String createdUser;
    private Long currentNum;    // 현재 참여 인원

    public RoomResponseDto(Long roomId, String teamName, Long count, String createdUser, Long currentNum) {
        this.roomId = roomId;
        this.teamName = teamName;
        this.count = count;
        this.createdUser = createdUser;
        this.currentNum = currentNum;
    }


}
