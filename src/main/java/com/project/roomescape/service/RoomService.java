package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.model.GameResource;
import com.project.roomescape.model.Room;
import com.project.roomescape.model.State;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.GameResourceRepository;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import com.project.roomescape.responseDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.project.roomescape.exception.ErrorCode.ROOM_MEMBER_FULL;
import static com.project.roomescape.exception.ErrorCode.ROOM_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GameResourceRepository gameResourceRepository;
    private final int ROOM_CAPACITY = 4;

    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {
        String teamName = roomRequestDto.getTeamName();
        String userId = roomRequestDto.getUserId();
        Room room = roomRepository.save(new Room(teamName, userId));

        String nickName = createNickName();
        String img = createImg("userImg");

        User user = User.createUser(room, nickName, img, userId);
        userRepository.save(user);

        log.info("roomId : " + room.getId() + "가 개설되었습니다!");

        String url = "/room/" + room.getId();

        List<UserResponseDto> userList = new ArrayList<>();
        userList.add(new UserResponseDto(nickName, img, userId));

        RoomResponseDto roomResponseDto = new RoomResponseDto(
                room.getId(), teamName, userId,
                1, url, userList, null);
        return roomResponseDto;
    }

    public RoomResponseDto getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));
        String url = "/room/" + roomId;

        List<UserResponseDto> userList = findUserList(roomId);
        RoomResponseDto roomResponseDto = new RoomResponseDto(
                room.getId(), room.getTeamName(), room.getCreatedUser(),
                room.getUserList().size(), url, userList, room.getStartAt());
        return roomResponseDto;
    }

    public List<RoomResponseDto> getRooms(int page) {
        List<RoomResponseDto> roomResponseDtoList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        int size = page * 4;
        Pageable pageable = PageRequest.of(0, size, sort);

        // 게임 종료된 방(State.CLOSE)는 제외
        Page<Room> roomList = roomRepository.findAllByState(pageable, State.ACTIVE);
        for(Room eachRoom : roomList){
            String url = "/room/" + eachRoom.getId();
            List<UserResponseDto> userList = findUserList(eachRoom.getId());
            RoomResponseDto roomResponseDto = new RoomResponseDto(
                    eachRoom.getId(), eachRoom.getTeamName(), eachRoom.getCreatedUser(),
                    eachRoom.getUserList().size(), url, userList, eachRoom.getStartAt());
            roomResponseDtoList.add(roomResponseDto);
        }
        return roomResponseDtoList;
    }

    // getRoom, getRooms에서 사용
    private List<UserResponseDto> findUserList(Long roomId) {
        List<UserResponseDto> userList = new ArrayList<>();
        List<User> users = userRepository.findAllByRoomId(roomId);
        for(User eachUser : users) {
            userList.add(new UserResponseDto(eachUser.getNickName(),
                    eachUser.getImg(), eachUser.getUserId()));
        }
        return userList;
    }

    public void joinRoom(Long roomId, RoomAddRequestDto roomAddRequestDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));

        if(room.getUserList().size() == ROOM_CAPACITY) {
            throw new CustomException(ROOM_MEMBER_FULL);
        } else {
            String userId = roomAddRequestDto.getUserId();
            List<User> userList = room.getUserList();

            // nickName 생성 및 중복확인
            String nickName = createNickName();
            List<String> nickNameList = new ArrayList<>();
            for (User user : userList) {
                nickNameList.add(user.getNickName());
            }
            while (nickNameList.contains(nickName)) {
                nickName = createNickName();
            }

            // img 생성 및 중복확인
            String img = createImg("userImg");
            List<String> imgList = new ArrayList<>();
            for (User user : userList) {
                imgList.add(user.getImg());
            }
            while (imgList.contains(img)) {
                img = createImg("userImg");
            }

            User user = User.createUser(room, nickName, img, userId);
            userRepository.save(user);

            log.info(userId + "가 " + roomId + "에 참여하였습니다!");
        }
    }

    private String createNickName() {
        List<String> nickNameList1 = new ArrayList<>(Arrays.asList(
                "잠자는 ", "졸고있는 ", "낮잠자는 ", "꿈꾸는 ", "가위눌린 ", "침 흘리는 ", "잠꼬대하는 "));
        List<String> nickNameList2 = new ArrayList<>(Arrays.asList(
                "다람쥐", "고양이", "호랑이", "쥐", "고등어", "토끼", "강아지", "나무늘보", "쿼카"));

        Random random = new Random();
        int num1 = random.nextInt(nickNameList1.size());
        int num2 = random.nextInt(nickNameList2.size());
        return nickNameList1.get(num1) + nickNameList2.get(num2);
    }

    private String createImg(String type) {
        List<GameResource> gameResourceList = gameResourceRepository.findAllByType(type);

        Random random = new Random();
        int num = random.nextInt(4);
        return gameResourceList.get(num).getUrl();
    }
}
