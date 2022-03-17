package com.project.roomescape.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RoomResponseDto {
    private Long roomId;
    private String teamName;
    private String createdUser;
    // 현재 참여 인원
    private int currentNum;
    private String url;
    private List<UserResponseDto> userList;
    private Long startAt;

}
