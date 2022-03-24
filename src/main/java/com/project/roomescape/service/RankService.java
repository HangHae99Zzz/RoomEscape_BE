package com.project.roomescape.service;

import com.project.roomescape.model.Rank;
import com.project.roomescape.repository.RankRepository;
import com.project.roomescape.responseDto.RankResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RankService {

    private final RankRepository rankRepository;

    // 전체 랭킹 조회하기 & 게임종료 후 랭킹 5개 조회하기 : 2가지 컨트롤러를 이 한번으로 처리함
    public List<RankResponseDto> getRanks(Long roomId) {
        // 반환할 responseDtoList
        List<RankResponseDto> rankResponseDtoList = new ArrayList<>();
        // 모든 ranklist를 찾고 (게임 플레이시간이 작은순으로 정렬)
        List<Rank> rankList = rankRepository.findAllByOrderByTimeAsc();
        //5개의 배열을 담을 새로운 ArrayList 또는 10개의 배열을 담을 새로운 ArrayList
        List<Rank> tempList = new ArrayList();
        int[] tempRankList = new int[5];
        // *** roomId가 있으면 : 0보다 크면 roomId가 있는거고 게임종료 후 랭킹 5개 조회하기 컨트롤러가 실행됨
        if (roomId > 0) {
            // i를 밑에 for문 말고 여기서 선언해줘야 빼서 쓸수가 있다.
            int i;
            // 특정한 roomId랑 같은 rank의 인덱스를 찾는다
            for (i = 0; i < rankList.size(); i++) {
                if (rankList.get(i).getRoomId().equals(roomId)) {
                    break;
                }
            }
            // i를 중심으로 +-2해서 총 5개의 배열을 하나씩 list에 담아준다
            // 현재에서 -2 순위
            tempList.add(rankList.get(i - 2));
            // 현재에서 -1 순위
            tempList.add(rankList.get(i - 1));
            // 현재  roomId에 해당하는 rank의 index
            tempList.add(rankList.get(i));
            // 현재에서 +1 순위
            tempList.add(rankList.get(i + 1));
            // 현재에서 +2 순위
            tempList.add(rankList.get(i + 2));
            // 순위만 저장하기
            tempRankList = new int[] {i - 2, i - 1, i, i + 1, i + 2};
        } else {
            for(int i = 0; i < rankList.size(); i++) {
                tempList.add(rankList.get(i));
                if(i == 11) break;
            }
        }
        //tempList를 rankList로 옮겨준다.
        rankList = tempList;
        // Long으로 한건 밑에서 계산을 편하게 하려고, 1값은 배열은 0부터 시작인데 순위는 1부터라서 1을 올려줘야해서 해준거다
        Long check = 1L;
        // 임의값 제거 (가짜 1 2 등 가짜 꼴지 1 2 등) : db에만 보여주고 실제로는 안보이게 할 거여서
        for (int i = 0; i < rankList.size() ; i++) {   //
            if (rankList.get(i).getTime().equals("00:00:00")) {
                check--;
            }
            if (roomId == -1) {
                if (rankList.get(i).getTime().equals("00:00:00")
                        || rankList.get(i).getTime().equals("99:99:99")) {
                    continue;
                }
            }
            Long rank = (roomId > 0) ? tempRankList[i] + check : i + check;
            // 해당 인덱스에서 보낼 것들을 찾는다
            String teamName = rankList.get(i).getTeamName();
            String time = rankList.get(i).getTime();
            int userNum = rankList.get(i).getUserNum();
            Long rankRoomId = rankList.get(i).getRoomId();

            RankResponseDto rankResponseDto = new RankResponseDto(
                    rankRoomId, rank, teamName, time, userNum);

            rankResponseDtoList.add(rankResponseDto);
        }
        return rankResponseDtoList;
    }

}