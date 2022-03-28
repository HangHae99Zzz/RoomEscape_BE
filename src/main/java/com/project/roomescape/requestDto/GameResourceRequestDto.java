package com.project.roomescape.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameResourceRequestDto {
    private String type;
    private String url;
    private String time;

}
