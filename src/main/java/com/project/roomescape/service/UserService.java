package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.model.Room;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.UserRequestDto;
import com.project.roomescape.responseDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.project.roomescape.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    // 유저 정보 조회하기
    public List<UserResponseDto> getUserInfo(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));

        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for(User user : room.getUserList()) {
            userResponseDtoList.add(new UserResponseDto(
                    user.getId(), user.getNickName(), user.getImg(), user.getUserId()));
        }
        return userResponseDtoList;
    }

    // 유저 삭제하기
    @Transactional
    public void deleteUser(UserRequestDto userRequestDto) {
        User user = userRepository.findUserByUserId(userRequestDto.getUserId());
//        User user = userRepository.findBy(id)
//                        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        //방을 만든 사람은 나갈 수 없음
        if (user.getNickName().equals(user.getRoom().getCreatedUser())) {
            throw new CustomException(USER_DELETE_FAIL);
        }
        userRepository.deleteUserByUserId(user.getUserId());
    }
}
