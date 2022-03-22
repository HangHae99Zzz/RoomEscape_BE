package com.project.roomescape.controller;

import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.requestDto.RankRequestDto;
import com.project.roomescape.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GameController {

    private final GameService gameService;

    // 게임 resource 저장하기
    @PostMapping("/games")
    public void saveGameResource(@RequestBody GameResourceRequestDto gameResourceRequestDto) {
        gameService.saveGameResource(gameResourceRequestDto);
    }

    // 게임 시작하기
    @PutMapping("/games/{roomId}")
    public void startGame(@PathVariable Long roomId) {
        gameService.startGame(roomId);
    }

    // 게임 종료하기
    @PostMapping("/games/{roomId}")
    public void gameOver(@PathVariable Long roomId, @RequestBody RankRequestDto rankRequestDto){
        gameService.gameOver(roomId, rankRequestDto);
    }

}
