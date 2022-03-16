package com.project.roomescape.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomResponseDto {
    private Long roomId;
    private String teamName;
    private String createdUser;
    private Integer currentNum;    // 현재 참여 인원
    private String url;
    private List<UserResponseDto> userList;
    private Long startAt;

    public RoomResponseDto(Long roomId, String teamName, String createdUser,
                           Integer currentNum, String url, List<UserResponseDto> userList, Long startAt) {
        this.roomId = roomId;
        this.teamName = teamName;
        this.createdUser = createdUser;
        this.currentNum = currentNum;
        this.url = url;
        this.userList = userList;
        this.startAt = startAt;
    }



}
