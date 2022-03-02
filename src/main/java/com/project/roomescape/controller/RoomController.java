package com.project.roomescape.controller;

import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;

    // 방 개설하기 // request를 받아
    @PostMapping("/register/room")
    public void createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        roomService.createRoom(roomRequestDto);
    }








}
