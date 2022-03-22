package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.exception.ErrorCode;
import com.project.roomescape.model.*;
import com.project.roomescape.repository.*;
import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.requestDto.RankRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameResourceRepository gameResourceRepository;
    private final RoomRepository roomRepository;
    private final QuizRepository quizRepository;
    private final ClueRepository clueRepository;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;


    // 게임 resource 저장하기
    public void saveGameResource(GameResourceRequestDto gameResourceRequestDto) {
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
    // room, user, clue, quiz 다 끊어줘야해)
    @Transactional
    public void gameOver(Long roomId, RankRequestDto rankRequestDto) {

        // teamName 찾기
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        String teamName = room.getTeamName();

        // time 찾기
        String time = rankRequestDto.getTime();

        // userNum찾기
        int userNum = room.getUserList().size();

        // 게임 종료 처리
        Pass pass = (rankRequestDto.isPass()) ? Pass.SUCCESS : Pass.FAIL;
        room.gameOver(pass, (long) userNum);

        // 게임 성공 시 rank 등록하기
        if (pass == Pass.SUCCESS) {
            Rank rank = new Rank(teamName, time, room.getId(), userNum);
            rankRepository.save(rank);
        }
        // user
        userRepository.deleteUserByRoomId(roomId);
        // clue
        clueRepository.deleteClueByRoomId(roomId);

    }

    public void startGame(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        room.setStartAt(System.currentTimeMillis());
        roomRepository.save(room);
    }

}
