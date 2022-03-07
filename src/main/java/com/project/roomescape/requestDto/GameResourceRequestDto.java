package com.project.roomescape.requestDto;

import lombok.Getter;

@Getter
public class GameResourceRequestDto {
    private String type;
    private String url;


    public GameResourceRequestDto(String type, String url) {
        this.type = type;
        this.url = url;


    }
}
