package com.project.roomescape.service;

import com.project.roomescape.model.Clue;
import com.project.roomescape.repository.ClueRepository;
import com.project.roomescape.responseDto.ClueResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.*;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClueService {

    private final ClueRepository clueRepository;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    // clue 생성하기
    @Transactional
    public void createClue(Long roomId) {
        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        String[] arr = new String[]{"+", "-"};

        // List 저장하기
//        clueRepository.saveAll(clueList);

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        sb.append("Clues").append(roomId);
        String keyName = sb.toString();

        Map<String, Object> map = new HashMap<>();
        map.put("clueBa1", String.valueOf(random.nextInt(3999) + 1000));
        map.put("clueBa2", String.valueOf(random.nextInt(3999) + 1000));
        map.put("clueBa3", arr[random.nextInt(1)]);

        hashOperations.putAll(keyName, map);

        log.info(roomId + "의 Clue가 생성되었습니다!");

    }


    // clue 조회하기
    public ClueResponseDto getClue(Long roomId, String clueType) {


        ClueResponseDto clueResponseDto;


        //Clue가 clue redis에 있는 정보면 찾아서 내려주고 그렇지 않다면 빈 문자열로 내려준다.
        if(clueType.equals("Ba1") || clueType.equals("Ba2") || clueType.equals("Ba3")) {
            clueResponseDto = findClue(clueType, roomId);
        } else {
            clueResponseDto = new ClueResponseDto("");
        }
        return clueResponseDto;
    }

    public ClueResponseDto findClue(String clueType, Long roomId) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        ClueResponseDto clueResponseDto;
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        sb1.append("Clues").append(roomId);
        String keyName = sb1.toString();

        sb2.append("clue").append(clueType);
        String clueName = sb2.toString();

        clueResponseDto = new ClueResponseDto(hashOperations.get(keyName, clueName).toString());
        return clueResponseDto;
    }
}
