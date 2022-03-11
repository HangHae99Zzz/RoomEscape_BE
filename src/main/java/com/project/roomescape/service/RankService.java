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








    // 전체 랭킹 조회하기
    public List<RankResponseDto> getRanks(Long roomId) {
        // 반환할 responseDtoList
        List<RankResponseDto> rankResponseDtoList = new ArrayList<>();

        // 모든 ranklist를 찾고
        List<Rank> rankList = rankRepository.findAllByOrderByTimeAsc(); // 게임종료 time이 적게걸린 순으로 정렬해서

        if (roomId > 0) {  // roomId가 있으면
            List<Rank> tempList = new ArrayList();

            // 해당 방의 index를 알아야하고 +-2 ranklist size 2이상상
            // time 초로 db 저장 int 1, 2 등은 -1 / 86400 (24시간)
            int i;
            for (i = 0; i < rankList.size(); i++) {

                if (rankList.get(i).getRoomId() == roomId) {
                    break;
                }
            }

            // index 찾기
            // +-2 배열을 새로만들기 : rankList
//            List<Rank> rankList = [rankList.get(i - 2), rankList.get(i-1), rankList.get(i), rankList.get(i+1), rankList.get(i+2)];

            tempList.add(rankList.get(i - 2));
            tempList.add(rankList.get(i - 1));
            tempList.add(rankList.get(i));
            tempList.add(rankList.get(i + 1));
            tempList.add(rankList.get(i + 2));
            rankList = tempList;
        }
            for (Rank eachRank : rankList) {
                // 임의값 제거 (가짜 1 2 등 가짜 꼴지 1 2 등)
                if(eachRank.getTime().equals("00:00:00") || eachRank.getTime().equals("99:99:99")){   // 이거 2개씩 총 4개 TestDataRunner에 넣어준다.
                    continue;
                }

                String teamName = eachRank.getTeamName();
                String time = eachRank.getTime();
                Integer userNum = eachRank.getUserNum();
                String comment = "";
                Long rankRoomId = eachRank.getRoomId();
                RankResponseDto rankResponseDto = new RankResponseDto(teamName, time, userNum, comment, rankRoomId);

                rankResponseDtoList.add(rankResponseDto);
            }

            return rankResponseDtoList;
        }



        // 코멘트 입력하기
        public void createComment (Long roomId, RankRequestDto rankRequestDto){
            String comment = rankRequestDto.getComment();

            Rank rank = rankRepository.findByRoomId(roomId);   // findById가 PK를 찾는거여서 roomId를 못찾는거였어...

            rank.setComment(comment); // comment만 set으로 업데이트해주면되지

            rankRepository.save(rank);

        }




    // Test용  랭킹 상위 1,2등 하위 1,2등 등록 메소드 - TestDataRunner에서 사용
    public void testRegisterTime(RankRequestDto rankRequestDto) {
        Rank rank = new Rank(rankRequestDto);
        rankRepository.save(rank);
    }





}