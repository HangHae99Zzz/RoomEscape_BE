package com.project.roomescape.controller;

import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.responseDto.GameLoadingResponseDto;
import com.project.roomescape.responseDto.UserResponseDto;
import com.project.roomescape.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    // 유저 정보 조회하기
    @GetMapping("/user/{roomId}")
    public List<UserResponseDto> getUserInfo(@PathVariable Long roomId) {
        return userService.getUserInfo(roomId);
    }

    // 유저 삭제하기
    @PostMapping("/user")
    public GameLoadingResponseDto deleteUser(@RequestBody RoomAddRequestDto roomAddRequestDto) {
        return userService.deleteUser(roomAddRequestDto);
    }

}
