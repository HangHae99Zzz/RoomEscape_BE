package com.project.roomescape.service;

import com.project.roomescape.model.Clue;
import com.project.roomescape.repository.ClueRepository;
import com.project.roomescape.responseDto.ClueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class ClueService {

    private final ClueRepository clueRepository;

    // clue 생성하기
    @Transactional
    public void createClue(Long roomId) {
        Random random = new Random();

        // clue 생성해서 List에 담기
        List<Clue> clueList = new ArrayList<>();
        String[] arr = new String[]{"+", "-"};

        clueList.add(new Clue(roomId, "Ba1", String.valueOf(random.nextInt(3999) + 1000)));
        clueList.add(new Clue(roomId, "Ba2", String.valueOf(random.nextInt(3999) + 1000)));
        clueList.add(new Clue(roomId, "Ba3", arr[random.nextInt(1)]));

        // List 저장하기
        clueRepository.saveAll(clueList);
    }


    // clue 조회하기
    public ClueResponseDto getClue(Long roomId, String clueType) {
        Clue clue = clueRepository.findByRoomIdAndAndType(roomId, clueType);

        ClueResponseDto clueResponseDto = new ClueResponseDto(clue.getContent());

        return clueResponseDto;
    }
}
