package com.project.roomescape.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomResponseDto {
    private Long roomId;
    private String teamName;
    private Long count;
    private String createdUser;
    private Integer currentNum;    // 현재 참여 인원
    private String url;
    private Long clueA;
    private Long clueB;
    private String clueC;
    private List<Object> userList;

    public RoomResponseDto(Long roomId, String teamName, Long count, String createdUser,
                           Integer currentNum, String url, Long clueA, Long clueB, String clueC, List<Object> userList) {
        this.roomId = roomId;
        this.teamName = teamName;
        this.count = count;
        this.createdUser = createdUser;
        this.currentNum = currentNum;
        this.url = url;
        this.clueA = clueA;
        this.clueB = clueB;
        this.clueC = clueC;
        this.userList = userList;
    }



}
