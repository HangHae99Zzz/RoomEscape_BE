package com.project.roomescape.controller;

import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import com.project.roomescape.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;

    // 방 개설하기 // request를 받아
    @PostMapping("/room")
    public void createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        roomService.createRoom(roomRequestDto);
    }


    // 방 조회하기
    @GetMapping("/room/{roomId}")
    public RoomResponseDto getRoom(@PathVariable Long roomId) {
        return roomService.getRoom(roomId);
    }


    // 방 참여하기
    @PostMapping("/room/{roomId}")
    public void addMember(@PathVariable Long roomId, @RequestBody RoomAddRequestDto roomAddRequestDto) {
        roomService.addMember(roomId, roomAddRequestDto);
    }



    // 방 리스트 조회하기
    @GetMapping("/rooms")
    public List<RoomResponseDto> getAllRooms() {
        return roomService.getAllRooms();
    }


    // 방 삭제하기
    @DeleteMapping("/room/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }
}
