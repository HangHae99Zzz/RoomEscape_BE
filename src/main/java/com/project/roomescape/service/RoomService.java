package com.project.roomescape.service;

import com.project.roomescape.model.Room;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public void createRoom(RoomRequestDto roomRequestDto) {
        // roomRepository.save(teamName, createdUser:방장이야 user의 nickName을 저장)
        String teamName = roomRequestDto.getTeamName();

        // User에 nickNameList 만들기
        List<String> nickNameList = new ArrayList<>(Arrays.asList("red", "blue", "yellow", "green"));
        User.registerNickNameList(nickNameList);

        // 방장 User의 nickName가져오기
        Random random = new Random();
        int num = random.nextInt(3);
        String nickName = nickNameList.get(num);
        String img = "";

        // 방 저장
        Room room = roomRepository.save(new Room(teamName, nickName));

        // 방장 User 저장
        User user = User.addUser(room, nickName, img);
        userRepository.save(user);

    }
}
