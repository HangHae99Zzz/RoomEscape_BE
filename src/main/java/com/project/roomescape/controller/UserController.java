package com.project.roomescape.controller;

import com.project.roomescape.responseDto.UserResponseDto;
import com.project.roomescape.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
