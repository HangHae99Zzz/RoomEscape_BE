package com.project.roomescape.controller;

import com.project.roomescape.requestDto.GameLoadingDto;
import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.requestDto.RankRequestDto;
import com.project.roomescape.responseDto.GameLoadingResponseDto;
import com.project.roomescape.responseDto.GameResourceResponseDto;
import com.project.roomescape.service.GameResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GameResourceController {

    private final GameResourceService gameResourceService;

    // 게임 resource 저장하기
    @PostMapping("/game")
    public void saveGameResource(@RequestBody GameResourceRequestDto gameResourceRequestDto) {
        gameResourceService.saveGameResource(gameResourceRequestDto);
    }

    // 게임 시작하기
    @GetMapping("/game/{roomId}")
    public GameResourceResponseDto getGameResource(@PathVariable Long roomId) {
        return gameResourceService.getGameResource(roomId);
    }

//    게임 로딩 체크하기.
    @PostMapping("/game/check")
    public GameLoadingResponseDto checkGameLoading(@RequestBody GameLoadingDto gameLoadingDto) {
        return gameResourceService.checkGameLoading(gameLoadingDto);
    }

    // 게임 종료하기
    @DeleteMapping("/game/{roomId}/ending")
    public void gameOver(@PathVariable Long roomId, @RequestBody RankRequestDto rankRequestDto){
        gameResourceService.gameOver(roomId, rankRequestDto);
    }




}
