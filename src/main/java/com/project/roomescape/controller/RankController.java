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


    // 랭킹 조회하기
    @GetMapping("/ranks")
    public List<RankResponseDto> getRanks() {
        return rankService.getRanks();
    }

}
