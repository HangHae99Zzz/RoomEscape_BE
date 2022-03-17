package com.project.roomescape.service;

import com.project.roomescape.model.Rank;
import com.project.roomescape.repository.RankRepository;
import com.project.roomescape.repository.RoomRepository;
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
                for (i = 0; i < rankList.size(); i++) { // for문 안에서 int i를 안쓴거는 for문 밖에서 i 사용하려고    // for each 해서 eachRank를 안한이유는 개별적인 rank가 아니라 특정한 roomId랑 같은 rank의 인덱스를 찾아야해서다
                    if (rankList.get(i).getRoomId() == roomId) {   // for문을 돌려서 그 중 roomId와 같은 index찾아서
                        break; // for문을 빠져나와
                    }
                }

//            List<Rank> rankList = [rankList.get(i - 2), rankList.get(i-1), rankList.get(i), rankList.get(i+1), rankList.get(i+2)];
                // i를 중심으로 +-2해서 총 5개의 배열을 하나씩 list에 담아준다
                tempList.add(rankList.get(i - 2));       // 현재에서 -2 순위
                tempList.add(rankList.get(i - 1));       // 현재에서 -1 순위
                tempList.add(rankList.get(i));          // 현재  roomId에 해당하는 rank의 index
                tempList.add(rankList.get(i + 1));      // 현재에서 +1 순위
                tempList.add(rankList.get(i + 2));      // 현재에서 +2 순위
                rankList = tempList;     // 이 tempList로 밑에 for문을 돌아서 값들을 구하려고 (게임종료 후 랭킹 5개 조회하기)
            }


            Long check = 1L; // Long으로 한건 밑에서 계산을 편하게 하려고, 1값은 배열은 0부터 시작인데 순위는 1부터라서 1을 올려줘야해서 해준거다
            for (int i=0; i<rankList.size(); i++) {   //
                // 임의값 제거 (가짜 1 2 등 가짜 꼴지 1 2 등) : db에만 보여주고 실제로는 안보이게 할 거여서


                Long rank = i + check; // total -1 한거네? 0위부터 시작하니 1을 올려 1위를 해줬는데 "00:00:00"이 2개 있으니 -2해줘서 -1을 결국은 한거지...
                String teamName = rankList.get(i).getTeamName();   // 해당 인덱스에서 보낼 것들을 찾는다

                if (rankList.get(i).getTime().equals("00:00:00")) {
                    check --;

                }

                if (roomId == -1) {
                    if (rankList.get(i).getTime().equals("00:00:00")
                            || rankList.get(i).getTime().equals("99:99:99")) {
                        continue;
                    }
                }

                String time = rankList.get(i).getTime();
                int userNum = rankList.get(i).getUserNum();
                Long rankRoomId = rankList.get(i).getRoomId();

                RankResponseDto rankResponseDto = new RankResponseDto(rankRoomId, rank, teamName, time, userNum);

                rankResponseDtoList.add(rankResponseDto);
            }
            return rankResponseDtoList;
        }









}