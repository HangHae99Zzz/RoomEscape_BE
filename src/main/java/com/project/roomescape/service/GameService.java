package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.exception.ErrorCode;
import com.project.roomescape.model.GameResource;
import com.project.roomescape.model.Pass;
import com.project.roomescape.model.Rank;
import com.project.roomescape.model.Room;
import com.project.roomescape.repository.*;
import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.requestDto.RankRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {

    private final GameResourceRepository gameResourceRepository;
    private final RoomRepository roomRepository;
    private final ClueRepository clueRepository;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;

    public void createGameResource(GameResourceRequestDto gameResourceRequestDto) {
        String type = gameResourceRequestDto.getType();
        String url = gameResourceRequestDto.getUrl();
        GameResource gameResource = new GameResource(type, url);
        gameResourceRepository.save(gameResource);
    }

    @Transactional
    public void endGame(Long roomId, RankRequestDto rankRequestDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        String teamName = room.getTeamName();
        String time = rankRequestDto.getTime();
        int userNum = room.getUserList().size();

        // 게임 성공시 pass가 true, 게임 실패시 pass가 false
        Pass pass = (rankRequestDto.isPass()) ? Pass.SUCCESS : Pass.FAIL;
        // 게임 종료 처리(state.ACTIVE -> state.CLOSE)
        room.endGame(pass, (long) userNum);

        log.info(roomId + "는 탈출에 " + pass + "하였습니다!");

        if (pass == Pass.SUCCESS) {
            Rank rank = new Rank(teamName, time, room.getId(), userNum);
            rankRepository.save(rank);
        }

        userRepository.deleteUserByRoomId(roomId);
        clueRepository.deleteClueByRoomId(roomId);
    }

    public void startGame(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        room.setStartAt(System.currentTimeMillis());
        roomRepository.save(room);

        log.info(roomId + "의 게임이 시작되었습니다!");
    }
}
