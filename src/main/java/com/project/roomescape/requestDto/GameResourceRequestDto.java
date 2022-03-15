package com.project.roomescape.requestDto;

import lombok.Getter;

@Getter
public class GameResourceRequestDto {
    private String type;
    private String url;
    private String time;

    public GameResourceRequestDto(String type, String url, String time) {
        this.type = type;
        this.url = url;
        this.time = time;


    }
}
