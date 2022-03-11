package com.project.roomescape.controller;

import com.project.roomescape.responseDto.ClueResponseDto;
import com.project.roomescape.service.ClueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ClueController {

    private final ClueService clueService;

    @PostMapping("/clue/{roomId}")
    public void createClue(@PathVariable Long roomId) {
        clueService.createClue(roomId);
    }

    @GetMapping("/clue/{roomId}")
    public List<ClueResponseDto> getClue(@PathVariable Long roomId) {
        return clueService.getClue(roomId);
    }
}
