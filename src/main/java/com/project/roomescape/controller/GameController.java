package com.project.roomescape.controller;

import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.requestDto.RankRequestDto;
import com.project.roomescape.service.ClueService;
import com.project.roomescape.service.GameService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GameController {

    private final GameService gameService;
    private final ClueService clueService;

    @ApiOperation(value = "GameResource 저장하기")
    @PostMapping("/games/resources")
    public void saveGameResource(@RequestBody GameResourceRequestDto gameResourceRequestDto) {
        gameService.saveGameResource(gameResourceRequestDto);
    }

    @ApiOperation(value = "게임 시작하기")
    @PutMapping("/games/{roomId}")
    public void startGame(@PathVariable Long roomId) {
        gameService.startGame(roomId);
        clueService.createClue(roomId);
    }

    @ApiOperation(value = "게임 종료하기")
    @PostMapping("/games/{roomId}")
    public void gameOver(@PathVariable Long roomId, @RequestBody RankRequestDto rankRequestDto){
        gameService.gameOver(roomId, rankRequestDto);
    }

}
