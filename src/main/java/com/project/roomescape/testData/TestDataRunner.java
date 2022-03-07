package com.project.roomescape.testData;

import com.project.roomescape.repository.GameResourceRepository;
import com.project.roomescape.requestDto.GameResourceRequestDto;
import com.project.roomescape.service.GameResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    GameResourceService gameResourceService;

    @Autowired
    GameResourceRepository gameResourceRepository;



    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 테스트용으로 h2에 저장할 상품들
        GameResourceRequestDto requestDto1 = new GameResourceRequestDto(
                "userP1",
                "https://test001-s3-bucket.s3.ap-northeast-2.amazonaws.com/deadpool.jpg");
        GameResourceRequestDto requestDto2 = new GameResourceRequestDto(
                "userP2",
                "https://test001-s3-bucket.s3.ap-northeast-2.amazonaws.com/miniun.jpg");
        GameResourceRequestDto requestDto3 = new GameResourceRequestDto(
                "userP3",
                "https://test001-s3-bucket.s3.ap-northeast-2.amazonaws.com/yoda.jpg");
        GameResourceRequestDto requestDto4 = new GameResourceRequestDto(
                "userP4",
                "https://test001-s3-bucket.s3.ap-northeast-2.amazonaws.com/zombie.jpg");
        GameResourceRequestDto requestDto5 = new GameResourceRequestDto(
                "gameRunFile",
                "https://test001-s3-bucket.s3.ap-northeast-2.amazonaws.com/Duck.gltf");



        // h2에 상품 저장
        gameResourceService.testRegisterProduct(requestDto1);
        gameResourceService.testRegisterProduct(requestDto2);
        gameResourceService.testRegisterProduct(requestDto3);
        gameResourceService.testRegisterProduct(requestDto4);
        gameResourceService.testRegisterProduct(requestDto5);
    }
}
