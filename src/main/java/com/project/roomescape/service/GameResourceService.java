package com.project.roomescape.service;

import com.project.roomescape.model.GameResource;
import com.project.roomescape.repository.GameResourceRepository;
import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.responseDto.GameResourceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GameResourceService {

    private final GameResourceRepository gameResourceRepository;




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





    // 게임 시작하기  (type이 gameRunFile인 url만을 찾아서 보내주기)
    public GameResourceResponseDto getGameResource() {
        // 모든 gameResource를 찾은 다음
        List<GameResource> gameResourceList = gameResourceRepository.findAll();
        // 반환할 responseDto를 null 값으로 선언
        GameResourceResponseDto gameResourceResponseDto = null;
        // for문을 돌려서
        for (GameResource eachGameResouce : gameResourceList) {
            // 각각의 url을 찾아서
            String url = eachGameResouce.getUrl();
            // 찾은 url중에서 type이 gameRunFile이면
            if (eachGameResouce.getType().equals("gameRunFile")) {
                // gameRunFile type의 url을 responseDto에 담아서
                gameResourceResponseDto = new GameResourceResponseDto(url);
            }
        }
        // 반환해준다.
        return gameResourceResponseDto;
    }






    // Test용  userP1~5,gameRunFile의 url 등록 메소드 - TestDataRunner에서 사용
    public void testRegisterProduct(GameResourceRequestDto gameResourceRequestDto) {
        GameResource gameResource = new GameResource(gameResourceRequestDto);
        gameResourceRepository.save(gameResource);


    }
}
