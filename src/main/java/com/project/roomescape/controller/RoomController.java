package com.project.roomescape.controller;

import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import com.project.roomescape.service.RoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;



    @ApiOperation(value = "Room 개설하기")
    @PostMapping("/rooms")
    public RoomResponseDto createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        return roomService.createRoom(roomRequestDto);
    }



    @ApiOperation(value = "Room 조회하기")
    @GetMapping("/rooms/{roomId}")
    public RoomResponseDto getRoom(@PathVariable Long roomId) {
        return roomService.getRoom(roomId);
    }



    @ApiOperation(value = "Room 참여하기")
    @PostMapping("/rooms/{roomId}")
    public void joinRoom(@PathVariable Long roomId, @RequestBody RoomAddRequestDto roomAddRequestDto) {
        roomService.joinRoom(roomId, roomAddRequestDto);
    }



    @ApiOperation(value = "Room 리스트 조회하기")
    @GetMapping("/rooms/pages/{page}")
    public List<RoomResponseDto> getRooms(@PathVariable int page) {
        return roomService.getRooms(page);
    }

}
