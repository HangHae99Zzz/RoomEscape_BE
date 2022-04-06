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

    // time을 기준으로 전체 랭킹 조회하기(상위 Top10) & 게임종료 후 5개 조회하기(현재 방의 랭킹을 기준으로 위아래 2개씩해서 총 5개) : 2가지 컨트롤러를 이 한번으로 처리함
    public List<RankResponseDto> getRanks(Long roomId) {
        // 반환할 responseDtoList 선언
        List<RankResponseDto> rankResponseDtoList = new ArrayList<>();
        // 모든 templist를 찾고 (게임 플레이시간이 작은순으로 정렬)
        List<Rank> tempList = rankRepository.findAllByOrderByTimeAsc();
        //5개의 배열을 담을 새로운 ArrayList 또는 10개의 배열을 담을 새로운 ArrayList
        List<Rank> rankList = new ArrayList();
        int[] tempRankList = new int[5];
        // *** roomId가 있으면 : 0보다 크면 roomId가 있는거고 게임종료 후 랭킹 5개 조회하기 컨트롤러가 실행됨
        if (roomId > 0) {
            // i를 밑에 for문 말고 여기서 선언해줘야 빼서 쓸수가 있다.
            int i;
            // 특정한 roomId랑 같은 rank의 인덱스를 찾는다
            for (i = 0; i < tempList.size(); i++) {
                if (tempList.get(i).getRoomId().equals(roomId)) {
                    break;
                }
            }
            // i를 중심으로 +-2해서 총 5개의 배열을 찾아서 ranklist에 추가해준다
            rankList.add(tempList.get(i - 2));
            rankList.add(tempList.get(i - 1));
            rankList.add(tempList.get(i));
            rankList.add(tempList.get(i + 1));
            rankList.add(tempList.get(i + 2));

            // 순위만 저장하기 (실제 랭크는 무조건 index에서 1 차감한 값이다)
            tempRankList = new int[] {i - 3, i - 2, i - 1, i, i + 1};
        } else {
            // 랭크 전체조회 컨트롤러가 실행
            // 상위 12개만 rankList에 담기
            for(int i = 0; i < tempList.size(); i++) {
                rankList.add(tempList.get(i));
                if(i == 11) break;
            }
        }

        // dto에 담기
        // 임의값 제거 (가짜 1 2 등 가짜 꼴지 1 2 등) : db에만 보여주고 실제로는 안보이게 할 거여서
        for (int i = 0; i < rankList.size() ; i++) {
            // 랭크 전체조회 컨트롤러 실행
            if (roomId == -1) {
                // 가짜 1 2 등 이나 가짜 꼴찌 1 2 등과 같으면 무시하고 for문을 돌아라
                if (rankList.get(i).getTime().equals("00:00:00")
                        || rankList.get(i).getTime().equals("99:99:99")) {
                    continue;
                }
            }

            //실제 랭크는 무조건 index에서 1 차감한 값이다.
            Long rank = (roomId > 0) ? tempRankList[i] : i - 1L;
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