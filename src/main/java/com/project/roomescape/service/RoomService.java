package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
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

import static com.project.roomescape.exception.ErrorCode.ROOM_MEMBER_FULL;
import static com.project.roomescape.exception.ErrorCode.ROOM_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final int ROOM_CAPACITY = 4;

    public void createRoom(RoomRequestDto roomRequestDto) {
        // roomRepository.save(teamName, createdUser:방장이야 user의 nickName을 저장)
        String teamName = roomRequestDto.getTeamName();

        // nickName 부여
        String nickName = getNickName();
        String img = "";

        // 방 저장
        Room room = roomRepository.save(new Room(teamName, nickName));

        // 방장 User 저장
        User user = User.addUser(room, nickName, img);
        userRepository.save(user);

    }

    public void addMember(Long roomId) {
        // 방 찾기
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));


        // 방의 인원을 확인하고
        if(room.getUserList().size() == ROOM_CAPACITY) {
            throw new CustomException(ROOM_MEMBER_FULL);
        } else {
            // nickName 부여
            String nickName = getNickName();
            String img = "";

            // user 정보를 해당 room에 추가
            // user 저장
            User user = User.addUser(room, nickName, img);
            userRepository.save(user);
        }
    }

    // nickNameList를 생성하고
    // 들어온 순서대로 nickName 반환하는 함수
    private String getNickName() {
        // User에 nickNameList 만들기
        List<String> nickNameList = new ArrayList<>(Arrays.asList("red", "blue", "yellow", "green"));

        // nickNameList에서 랜덤으로 nickName 가져오기
        Random random = new Random();
        int num = random.nextInt(nickNameList.size());
        return nickNameList.get(num);
    }
}
