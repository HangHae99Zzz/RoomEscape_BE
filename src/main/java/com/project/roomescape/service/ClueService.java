package com.project.roomescape.service;

import com.project.roomescape.model.Clue;
import com.project.roomescape.repository.ClueRepository;
import com.project.roomescape.responseDto.ClueResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClueService {

    private final ClueRepository clueRepository;

    @Transactional
    public void createClue(Long roomId) {
        Random random = new Random();
        List<Clue> clueList = new ArrayList<>();
        String[] plusOrMinus = new String[]{"+", "-"};

        clueList.add(new Clue(roomId, "Ba1", String.valueOf(random.nextInt(3999) + 1000)));
        clueList.add(new Clue(roomId, "Ba2", String.valueOf(random.nextInt(3999) + 1000)));
        clueList.add(new Clue(roomId, "Ba3", plusOrMinus[random.nextInt(1)]));

        clueRepository.saveAll(clueList);

        log.info(roomId + "의 Clue가 생성되었습니다!");
    }

    public ClueResponseDto getClue(Long roomId, String clueType) {
        ClueResponseDto clueResponseDto;

        //Clue가 clue entity에 있는 정보면 찾아서 내려주고 그렇지 않다면 빈 문자열로 내려준다.
        if(clueType.equals("Ba1") || clueType.equals("Ba2") || clueType.equals("Ba3")) {
            Clue clue = clueRepository.findByRoomIdAndType(roomId, clueType);
            clueResponseDto = new ClueResponseDto(clue.getContent());
        } else {
            clueResponseDto = new ClueResponseDto("");
        }
        return clueResponseDto;
    }
}
