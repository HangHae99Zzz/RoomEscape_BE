package com.project.roomescape.responseDto;

import lombok.Getter;

@Getter
public class GameResourceResponseDto {
    private String url;

    public GameResourceResponseDto(String url) {
        this.url = url;

    }
}
