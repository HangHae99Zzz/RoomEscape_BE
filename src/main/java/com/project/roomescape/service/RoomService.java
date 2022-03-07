package com.project.roomescape.service;




import com.project.roomescape.exception.CustomException;

import com.project.roomescape.model.Room;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;

import static com.project.roomescape.exception.ErrorCode.ROOM_MEMBER_FULL;
import static com.project.roomescape.exception.ErrorCode.ROOM_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final int ROOM_CAPACITY = 4;


    // 방 개설하기 //
    public void createRoom(RoomRequestDto roomRequestDto) {
        // roomRepository.save(teamName, createdUser:방장이야 user의 nickName을 저장)
        String teamName = roomRequestDto.getTeamName();
        String userId = roomRequestDto.getUserId();
        // nickName 부여
        String nickName = getNickName();
        String img = "";

        // 방 저장
        Room room = roomRepository.save(new Room(teamName, nickName)); // createdUser, 생성자 사용하는 방법 , 순서대로 간다. 이름달라도 된다.

        // 방장 User 저장
        User user = User.addUser(room, nickName, img, userId);
        userRepository.save(user);
    }



    // 방 조회하기    // 대기페이지, 게임페이지
    public RoomResponseDto getRoom(Long roomId) {

        // room을 찾는다
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));
        // room 안에 있는 값들을 get해서 구한다
        String teamName = room.getTeamName();
        Long count = room.getCount();
        String createdUser = room.getCreatedUser();
        // currentNum는 userList에서 size()를 통해 구해준다
        Integer currentNum = room.getUserList().size();

        //roomResponseDto에 해당하는 것들을 다 담아준다
        RoomResponseDto roomResponseDto = new RoomResponseDto(room.getId(), teamName, count, createdUser, currentNum );
        //roomResponseDto를 리턴해준다.
        return roomResponseDto;
    }




    // 방 리스트 조회하기
    public List<RoomResponseDto> getAllRooms() {

        List<RoomResponseDto> roomResponseDtoList = new ArrayList<>();

        List<Room> roomList = roomRepository.findAll();
        for(Room eachRoom : roomList){
            String teamName = eachRoom.getTeamName();
            Long count = eachRoom.getCount();
            String createdUser = eachRoom.getCreatedUser();
            Integer currentNum = eachRoom.getUserList().size();

            RoomResponseDto roomResponseDto = new RoomResponseDto(eachRoom.getId(), teamName, count, createdUser, currentNum );

            roomResponseDtoList.add(roomResponseDto);
        }
        return roomResponseDtoList;
    }


    // 방 삭제하기
    public void deleteRoom(Long roomId) {
        // room을 찾는다
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));
        // room에 있는 모든 user들을 찾아줘야 해서 userList를 찾는다
        List<User> userList = room.getUserList();
        // room과 userList를 각각 지워준다
        userRepository.deleteAll(userList);
        roomRepository.delete(room);
    }


    // 방 참여하기
    public void addMember(Long roomId, RoomAddRequestDto roomAddRequestDto) {
        // 방 찾기
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));

        // 방의 인원을 확인하고
        if(room.getUserList().size() == ROOM_CAPACITY) {
            throw new CustomException(ROOM_MEMBER_FULL);
        } else {
            // nickName 부여
            String userId = roomAddRequestDto.getUserId();
            String nickName = getNickName();
            String img = "";

            // user 정보를 해당 room에 추가
            // user 저장
            User user = User.addUser(room, nickName, img, userId);
            userRepository.save(user);
        }
    }

    private String getNickName() {
        // User에 nickNameList 만들기
        List<String> nickNameList = new ArrayList<>(Arrays.asList("red", "blue", "yellow", "green"));

        // nickNameList에서 랜덤으로 nickName 가져오기
        Random random = new Random();
        int num = random.nextInt(nickNameList.size());
        return nickNameList.get(num);
    }
}
