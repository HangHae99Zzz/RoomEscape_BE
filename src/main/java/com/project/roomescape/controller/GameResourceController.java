package com.project.roomescape.controller;

import com.project.roomescape.requestDto.GameLoadingDto;
import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.responseDto.GameLoadingResponseDto;
import com.project.roomescape.responseDto.GameResourceResponseDto;
import com.project.roomescape.service.GameResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/game/room")
    public GameResourceResponseDto getGameResource() {
        return gameResourceService.getGameResource();
    }

    @PostMapping("/game/check")
    public GameLoadingResponseDto checkGameLoading(@RequestBody GameLoadingDto gameLoadingDto) {
        return gameResourceService.checkGameLoading(gameLoadingDto);
    }
}
