package com.project.roomescape.controller;

import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;

    // 방 개설하기 // request를 받아
    @PostMapping("/register/room")
    public void createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        roomService.createRoom(roomRequestDto);
    }

    // 방 참여하기
    @PostMapping("/room/{roomId}")
    public void addMember(@PathVariable Long roomId) {
        roomService.addMember(roomId);
    }










}
