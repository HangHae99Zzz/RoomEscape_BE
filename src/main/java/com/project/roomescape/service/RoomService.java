package com.project.roomescape.service;


import com.project.roomescape.model.Room;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;


    // 방 개설하기 // 가은님
    public void createRoom(RoomRequestDto roomRequestDto) {
        // roomRepository.save(teamName, createdUser:방장이야 user의 nickName을 저장)
        String teamName = roomRequestDto.getTeamName();
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



}
