package com.project.roomescape.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponseDto {
    Long id;
    String nickName;
    String img;
    String userId;
}
