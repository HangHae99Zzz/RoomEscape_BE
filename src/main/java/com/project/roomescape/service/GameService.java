package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.exception.ErrorCode;
import com.project.roomescape.model.*;
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


    // 게임 resource 저장하기
    public void createGameResource(GameResourceRequestDto gameResourceRequestDto) {
        // requestDto로 받은 type을 찾아준다
        String type = gameResourceRequestDto.getType();
        // requestDto로 받은 url을 찾아준다
        String url = gameResourceRequestDto.getUrl();
        // gameResource 인스턴스에 담아준다
        GameResource gameResource = new GameResource(type, url);
        // repository에 저장해준다
        gameResourceRepository.save(gameResource);
    }



    // 게임 종료하기
    @Transactional
    public void endGame(Long roomId, RankRequestDto rankRequestDto) {
        // roomId와 같은 room을 찾는다(예외처리)
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        // teamName 찾기
        String teamName = room.getTeamName();
        // time 찾기
        String time = rankRequestDto.getTime();
        // userNum찾기
        int userNum = room.getUserList().size();

        // 게임 성공시 pass가 true, 게임 실패시 pass가 false
        // requestDto로 받은 pass가 true면 SUCCESS, pass가 false면 FAIL
        Pass pass = (rankRequestDto.isPass()) ? Pass.SUCCESS : Pass.FAIL;
        // 게임 종료 처리(state.ACTIVE -> state.CLOSE)
        room.endGame(pass, (long) userNum);

        // log에 게임 종료 띄어준다
        log.info(roomId + "는 탈출에 " + pass + "하였습니다!");

        // 게임 성공 시 rank 등록하기
        if (pass == Pass.SUCCESS) {
            // rank 인스턴스에 담아준다
            Rank rank = new Rank(teamName, time, room.getId(), userNum);
            // repository에 저장해준다
            rankRepository.save(rank);
        }

        // user 삭제 (room, quiz는 미래 통계를 위해 삭제하지 않고 보관)
        userRepository.deleteUserByRoomId(roomId);
        // clue 삭제
        clueRepository.deleteClueByRoomId(roomId);
    }


    // 게임 시작하기  // 게임시작시간을 새로 변경 후 저장하는거라 PutMapping 사용
    public void startGame(Long roomId) {
        // roomId로 room을 찾는다(예외처리)
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        // startAt을 현재시각(밀리세컨드 단위로)으로 변경한다
        room.setStartAt(System.currentTimeMillis());
        // repository에 저장한다
        roomRepository.save(room);
        // log에 몇번방이 시작했다고 띄어준다
        log.info(roomId + "의 게임이 시작되었습니다!");
    }

}
