package com.project.roomescape.controller;

import com.project.roomescape.responseDto.ClueResponseDto;
import com.project.roomescape.service.ClueService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ClueController {

    private final ClueService clueService;

    @ApiOperation(value = "Clue 조회하기")
    @GetMapping("/rooms/{roomId}/clues/{clueType}")
    public ClueResponseDto getClue(@PathVariable Long roomId, @PathVariable String clueType) {
        return clueService.getClue(roomId, clueType);
    }
}
