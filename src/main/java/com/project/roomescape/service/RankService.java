package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.exception.ErrorCode;
import com.project.roomescape.model.Rank;
import com.project.roomescape.model.Room;
import com.project.roomescape.repository.RankRepository;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.requestDto.RankRequestDto;
import com.project.roomescape.responseDto.RankResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RankService {

    private final RoomRepository roomRepository;
    private final RankRepository rankRepository;



    // 걸린 시간 등록하기
    public void createRanks(RankRequestDto rankRequestDto, Long roomId) {

//        // teamName 찾기
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        String teamName = room.getTeamName();


        // time 찾기
        String time = rankRequestDto.getTime();

        // userNum찾기
        Integer userNum = room.getUserList().size();


        String comment = "";

        Rank rank = new Rank(teamName, time, room.getId(), userNum, comment);
        rankRepository.save(rank);
    }



    // 랭킹 조회하기
    public List<RankResponseDto> getRanks() {
        // 반환할 responseDtoList
        List<RankResponseDto> rankResponseDtoList = new ArrayList<>();

        // 모든 ranklist를 찾고
        List<Rank> rankList = rankRepository.findAll();
        for (Rank eachRank : rankList) {
            String teamName = eachRank.getTeamName();
            String time = eachRank.getTime();
            Integer userNum = eachRank.getUserNum();
            String comment = "";
            RankResponseDto rankResponseDto = new RankResponseDto(teamName, time, userNum, comment);

            rankResponseDtoList.add(rankResponseDto);
        }
        return rankResponseDtoList;
    }



    // 코멘트 입력하기
    public void createComment(Long roomId, RankRequestDto rankRequestDto) {
        String comment = rankRequestDto.getComment();

        Rank rank = rankRepository.findByRoomId(roomId);   // findById가 PK를 찾는거여서 roomId를 못찾는거였어...

        rank.setComment(comment); // comment만 set으로 업데이트해주면되지

        rankRepository.save(rank);

    }
}
