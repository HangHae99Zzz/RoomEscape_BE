package com.project.roomescape.controller;


import com.project.roomescape.model.User;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.responseDto.GameLoadingResponseDto;
import com.project.roomescape.responseDto.UserResponseDto;
import com.project.roomescape.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 유저 정보 조회하기
//    @GetMapping("/user/{roomId}")
//    public List<UserResponseDto> getUserInfo(@PathVariable Long roomId) {
//        return userService.getUserInfo(roomId);
//    }

    // 유저 삭제하기

    @PostMapping("/user")
    public GameLoadingResponseDto deleteUser(@RequestBody RoomAddRequestDto roomAddRequestDto) {
        return userService.deleteUser(roomAddRequestDto);
    }
}
