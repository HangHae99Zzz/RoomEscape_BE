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


        Rank rank = new Rank(teamName, time, room.getId(), userNum);
        rankRepository.save(rank);
    }




    // 전체 랭킹 조회하기 & 게임종료 후 랭킹 5개 조회하기 : 2가지 컨트롤러를 이 한번으로 처리함
    public List<RankResponseDto> getRanks(Long roomId) {
        // 반환할 responseDtoList
        List<RankResponseDto> rankResponseDtoList = new ArrayList<>();

        // 모든 ranklist를 찾고
        List<Rank> rankList = rankRepository.findAllByOrderByTimeAsc(); // 게임 플레이시간이 작은순으로 정렬

            if (roomId > 0) {  // *** roomId가 있으면 : 0보다 크면 roomId가 있는거고 게임종료 후 랭킹 5개 조회하기 컨트롤러가 실행되는거고,
                               // roomId가 0보다 같거나 작으면 roomId가 없는거라 전체 랭킹 조회하기 컨트롤러가 실행   *****

                List<Rank> tempList = new ArrayList();  // +-2해서 총 5개의 배열을 담을 새로운 ArrayList

                // 해당 방의 index를 알아야해
                int i; // i를 밑에 for문 말고 여기서 선언해줘야 빼서 쓸수가 있다.
                for (i = 0; i < rankList.size(); i++) { // for문 안에서 int i를 안쓴거는 for문 밖에서 i 사용하려고
                    if (rankList.get(i).getRoomId() == roomId) {   // for문을 돌려서 그 중 roomId와 같은 index찾아서
                        break; // for문을 빠져나와
                    }
                }

//            List<Rank> rankList = [rankList.get(i - 2), rankList.get(i-1), rankList.get(i), rankList.get(i+1), rankList.get(i+2)];
                // i를 중심으로 +-2해서 총 5개의 배열을 하나씩 list에 담아준다
                tempList.add(rankList.get(i - 2));
                tempList.add(rankList.get(i - 1));
                tempList.add(rankList.get(i));
                tempList.add(rankList.get(i + 1));
                tempList.add(rankList.get(i + 2));
                rankList = tempList;     // 두 리스트가 같다고 해서 밑에 for문을 돌 수 있게 해준다
            }

            Long check = 1L;
            for (int i=0; i<rankList.size(); i++) {
                // 임의값 제거 (가짜 1 2 등 가짜 꼴지 1 2 등) : db에만 보여주고 실제로는 안보이게 할 거여서

                if (rankList.get(i).getTime().equals("00:00:00")) {
                    check --;
                    // 이 임의값은 무시하고 진행하라는거지
                    continue;
                } else if (rankList.get(i).getTime().equals("99:99:99")) {   // 이거 2개씩 총 4개 TestDataRunner에 넣어줬다.
                    continue;
                }

                Long rank = i + check;
                String teamName = rankList.get(i).getTeamName();
                String time = rankList.get(i).getTime();
                Integer userNum = rankList.get(i).getUserNum();
                Long rankRoomId = rankList.get(i).getRoomId();
                RankResponseDto rankResponseDto = new RankResponseDto(rankRoomId, rank, teamName, time, userNum);

                rankResponseDtoList.add(rankResponseDto);
            }
            return rankResponseDtoList;
        }




//    // Test용  랭킹 상위 1,2등 하위 1,2등 등록 메소드 - TestDataRunner에서 사용
//    public void testRegisterTime(RankRequestDto rankRequestDto) {
//        Rank rank = new Rank(rankRequestDto);
//        rankRepository.save(rank);
//    }





}