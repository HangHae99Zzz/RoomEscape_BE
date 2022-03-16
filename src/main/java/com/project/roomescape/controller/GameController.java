package com.project.roomescape.controller;

import com.project.roomescape.requestDto.GameLoadingDto;
import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.requestDto.RankRequestDto;
import com.project.roomescape.responseDto.GameLoadingResponseDto;
import com.project.roomescape.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GameController {

    private final GameService gameService;

    // 게임 resource 저장하기
    @PostMapping("/game")
    public void saveGameResource(@RequestBody GameResourceRequestDto gameResourceRequestDto) {
        gameService.saveGameResource(gameResourceRequestDto);
    }

    // 게임 시작하기
    @PostMapping("/game/{roomId}")
    public void startGame(@PathVariable Long roomId) {
        gameService.startGame(roomId);
    }

    // 게임 시작하기
//    @GetMapping("/game/room")
//    public GameResourceResponseDto getGameResource() {
//        return gameResourceService.getGameResource();
//    }

//    게임 로딩 체크하기.
//    @PostMapping("/game/check")
//    public GameLoadingResponseDto checkGameLoading(@RequestBody GameLoadingDto gameLoadingDto) {
//        return gameService.checkGameLoading(gameLoadingDto);
//    }


    // 게임 종료하기
    @PostMapping("/game/{roomId}/ending")
    public void gameOver(@PathVariable Long roomId, @RequestBody RankRequestDto rankRequestDto){
        gameService.gameOver(roomId, rankRequestDto);
    }



}
