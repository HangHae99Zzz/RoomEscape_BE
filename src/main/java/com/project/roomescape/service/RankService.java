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

    public List<RankResponseDto> getRanks(Long roomId) {
        List<Rank> allRankList = rankRepository.findAllByOrderByTimeAsc();
        List<Rank> rankList = new ArrayList();

        // 게임종료 후 rank 조회하기
        int[] tempRankList = new int[5];
        if (roomId > 0) {
            int i;
            for (i = 0; i < allRankList.size(); i++) {
                if (allRankList.get(i).getRoomId().equals(roomId)) {
                    break;
                }
            }
            rankList.add(allRankList.get(i - 2));
            rankList.add(allRankList.get(i - 1));
            rankList.add(allRankList.get(i));
            rankList.add(allRankList.get(i + 1));
            rankList.add(allRankList.get(i + 2));
            tempRankList = new int[]{i - 3, i - 2, i - 1, i, i + 1};
            // 전체 rank 조회하기
        } else {
            for (int i = 0; i < allRankList.size(); i++) {
                rankList.add(allRankList.get(i));
                if (i == 11) break;
            }
        }

        List<RankResponseDto> rankResponseDtoList = new ArrayList<>();
        for (int i = 0; i < rankList.size(); i++) {
            if (roomId == -1) {
                // 최소 5개 rank 데이터를 출력하기 위한 임의값은 전체 rank 조회하기 dto에는 제외
                if (rankList.get(i).getTime().equals("00:00:00")
                        || rankList.get(i).getTime().equals("99:99:99")) {
                    continue;
                }
            }

            // 게임 종료 후 rank 조회하기에서는 순위를 위에서 저장한 tempRankList에서 가져옴
            Long rank = (roomId > 0) ? tempRankList[i] : i - 1L;
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