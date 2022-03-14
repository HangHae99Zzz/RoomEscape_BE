package com.project.roomescape.service;

import com.project.roomescape.model.Room;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.responseDto.GameLoadingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final GameResourceService gameResourceService;

    // 유저 정보 조회하기
//    public List<UserResponseDto> getUserInfo(Long roomId) {
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));
//
//        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
//        for(User user : room.getUserList()) {
//            userResponseDtoList.add(new UserResponseDto(
//                    user.getId(), user.getNickName(), user.getImg(), user.getUserId()));
//        }
//        return userResponseDtoList;
//    }

    // 유저 삭제하기
    @Transactional
    public GameLoadingResponseDto deleteUser(RoomAddRequestDto roomAddRequestDto) {
        GameLoadingResponseDto gameLoadingResponseDto= new GameLoadingResponseDto();
//        나간 유저 정보.
        Optional<User> user = userRepository.findByUserId(roomAddRequestDto.getUserId());

        if(!user.isPresent()) {
            gameLoadingResponseDto.setUserId(null);
            gameLoadingResponseDto.setCheck(null);
            return gameLoadingResponseDto;
        }

//        나가는 방의 기존 방장
        Room room = user.get().getRoom();
        room.getUserList().remove(user);

//        유저 삭제
        userRepository.deleteUserByUserId(roomAddRequestDto.getUserId());




//        방장인 경우
        if (roomAddRequestDto.getUserId().equals(room.getCreatedUser())) {
            room.changeOwner();
            Room changedRoom = roomRepository.save(room);
            gameLoadingResponseDto.setUserId(changedRoom.getCreatedUser());
        } else{
            gameLoadingResponseDto.setUserId(null);
        }

//        나갔는데 만약 게임 로딩중에 나간경우.

        if(gameResourceService.exitDuringLoading(room)) {
            gameLoadingResponseDto.setCheck("true");
            room.setStartAt(System.currentTimeMillis());
            roomRepository.save(room);
        } else {
            gameLoadingResponseDto.setCheck(null);
        }
        return gameLoadingResponseDto;

    }
}
