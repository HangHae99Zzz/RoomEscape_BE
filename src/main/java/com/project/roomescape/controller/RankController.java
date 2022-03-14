package com.project.roomescape.controller;


import com.project.roomescape.requestDto.RankRequestDto;
import com.project.roomescape.responseDto.RankResponseDto;
import com.project.roomescape.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RankController {

    private final RankService rankService;

    // 걸린 시간 등록하기
    @PostMapping("/ranks/{roomId}")
    public void createRanks(@RequestBody RankRequestDto rankRequestDto, @PathVariable Long roomId) {
        rankService.createRanks(rankRequestDto, roomId);
    }


    // 전체 랭킹 조회하기
    @GetMapping("/ranks")
    public List<RankResponseDto> getRanks() {
        Long roomId = -1L;                      //  roomId에 null이면 오류떠서 -1 이나 0 넣어서 선언하면된다.
        return rankService.getRanks(roomId);    //  밑에 꺼랑 같은 getRanks를 쓰기위해서 roomId를 넣어주는거다
    }
    // 위 아래 같은 getRanks 사용!

    // 게임 종료 후 랭킹 5개 조회하기
    @GetMapping("/rank/{roomId}")
    public List<RankResponseDto> getRanks(@PathVariable Long roomId) {
        return rankService.getRanks(roomId);
    }




}
