package com.project.roomescape.controller;

import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.requestDto.RankRequestDto;
import com.project.roomescape.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    // 게임 종료하기
    @PostMapping("/game/{roomId}/ending")
    public void gameOver(@PathVariable Long roomId, @RequestBody RankRequestDto rankRequestDto){
        gameService.gameOver(roomId, rankRequestDto);
    }

}
