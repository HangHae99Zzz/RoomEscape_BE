package com.project.roomescape.service;




import com.project.roomescape.exception.CustomException;

import com.project.roomescape.model.Room;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Random;

import static com.project.roomescape.exception.ErrorCode.ROOM_MEMBER_FULL;
import static com.project.roomescape.exception.ErrorCode.ROOM_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final int ROOM_CAPACITY = 4;


    // 방 개설하기 // 가은님
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



    // 방 조회하기    // 대기페이지, 게임페이지
    public RoomResponseDto getRoom(Long roomId) {
//        (v)private Long roomId;
//        (v) private String teamName;
//        (v) private Long count;
//      (v)  private String createdUser;   방 개설할 때 처음엔 애 혼자니 user nickname을 넣으면 그게 createdUser인거지
//        private Long currentNum;     userList.size() 이거해서 구해
//        이 5개 찾아서 roomResponseDto에 담아서 리턴하면되

        Room room = roomRepository.getById(roomId);
        String teamName = room.getTeamName();
        Long count = room.getCount();
        String createdUser = room.getCreatedUser();
        Long currentNum = room.getUserList().size();

        return new RoomResponseDto();






    }



//
//    // 방 리스트 조회하기
//    public List<RoomResponseDto> getAllRooms() {
////        private Long roomId;
////        private String teamName;
////        private Long count;
////        private String createdUser;
////        private Long currentNum;
////        이 5개 찾아서 roomResponseDtoList로 만들어 담아서 리턴하면되
//
//
//    }


    // 방 삭제하기
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new IllegalArgumentException("방을 찾을 수 없습니다."));
        User user = userRepository.findById(roomId)
                .orElseThrow(()-> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        List<User> userList = user.getRoom().getUserList();
        roomRepository.delete(room);
        userRepository.delete(user);
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
