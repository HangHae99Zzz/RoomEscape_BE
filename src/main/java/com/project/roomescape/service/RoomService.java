package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.model.GameResource;
import com.project.roomescape.model.Room;
import com.project.roomescape.model.User;
import com.project.roomescape.repository.GameResourceRepository;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.repository.UserRepository;
import com.project.roomescape.requestDto.RoomAddRequestDto;
import com.project.roomescape.requestDto.RoomRequestDto;
import com.project.roomescape.responseDto.RoomResponseDto;
import com.project.roomescape.responseDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GameResourceRepository gameResourceRepository;
    // 상수 선언
    private final int ROOM_CAPACITY = 4;

    // 방 개설하기 //
    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {

        // roomRepository.save(teamName, createdUser:방장이야 user의 nickName을 저장)
        String teamName = roomRequestDto.getTeamName();
        String userId = roomRequestDto.getUserId();

        // 방 저장
        Room room = roomRepository.save(new Room(teamName, userId)); // createdUser, 생성자 사용하는 방법 , 순서대로 간다. 이름달라도 된다.

        // nickName 부여
        String nickName = getNickName();
        String type = "userImg";
        String img = getImg(type);

        // 방장 User 저장
        User user = User.addUser(room, nickName, img, userId);
        userRepository.save(user);

        String url = "/room/" + room.getId();

        List<UserResponseDto> userList = new ArrayList<>();
        userList.add(new UserResponseDto(nickName, img));

        //roomResponseDto에 해당하는 것들을 다 담아준다
        RoomResponseDto roomResponseDto = new RoomResponseDto(
                room.getId(), teamName, userId,
                1, url, userList, null);

        //roomResponseDto를 리턴해준다.
        return roomResponseDto;
    }

    // 방 조회하기    // 대기페이지, 게임페이지
    public RoomResponseDto getRoom(Long roomId) {
        // room을 찾는다
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));
        String url = "/room/" + roomId;

        // user for문 돌려서 다 찾아서 보내야해
        List<UserResponseDto> userList = new ArrayList<>();
        List<User> users = userRepository.findAllByRoomId(roomId);
        for(User eachUser : users) {
            userList.add(new UserResponseDto(eachUser.getNickName(), eachUser.getImg()));
        }

        //roomResponseDto에 해당하는 것들을 다 담아준다
        RoomResponseDto roomResponseDto = new RoomResponseDto(
                room.getId(), room.getTeamName(), room.getCreatedUser(),
                room.getUserList().size(), url, userList, room.getStartAt());

         //roomResponseDto를 리턴해준다.
        return roomResponseDto;
    }


    // 방 리스트 조회하기
    public List<RoomResponseDto> getAllRooms(int page) {

        List<RoomResponseDto> roomResponseDtoList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        int size = page * 4;

        Pageable pageable = PageRequest.of(0, size, sort);

        Page<Room> roomList = roomRepository.findAll(pageable);
        for(Room eachRoom : roomList){
            String url = "/room/" + eachRoom.getId();

            // user for문 돌려서 다 찾아서 보내야해
            List<UserResponseDto> userList = new ArrayList<>();
            List<User> users = userRepository.findAllByRoomId(eachRoom.getId());
            for(User eachUser : users) {
                userList.add(new UserResponseDto(eachUser.getNickName(), eachUser.getImg()));
            }

            RoomResponseDto roomResponseDto = new RoomResponseDto(
                    eachRoom.getId(), eachRoom.getTeamName(), eachRoom.getCreatedUser(),
                    eachRoom.getUserList().size(), url, userList, eachRoom.getStartAt());

            roomResponseDtoList.add(roomResponseDto);
        }
        return roomResponseDtoList;
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
            List<User> userList = room.getUserList();
            String nickName = getNickName();

            // nickName 중복확인
            List<String> nickNameList = new ArrayList<>();
            for (User user : userList) {
                nickNameList.add(user.getNickName());
            }
            while (nickNameList.contains(nickName)) {
                nickName = getNickName();
            }

            String type = "userImg";
            String img = getImg(type);
            // img 중복확인
            List<String> imgList = new ArrayList<>();
            for (User user : userList) {
                imgList.add(user.getImg());
            }
            while (imgList.contains(img)) {
                img = getImg(type);
            }

            // user 정보를 해당 room에 추가
            // user 저장
            User user = User.addUser(room, nickName, img, userId);
            userRepository.save(user);
        }
    }


    public String getNickName() {
        // User에 nickNameList 만들기
        List<String> nickNameList = new ArrayList<>(Arrays.asList(
                "잠자는", "졸고있는", "낮잠자는", "꿈꾸는", "가위눌린", "침 흘리는", "잠꼬대하는"));
        List<String> nickNameList2 = new ArrayList<>(Arrays.asList(
                "다람쥐", "고양이", "호랑이", "쥐", "고등어", "토끼", "강아지", "나무늘보", "쿼카"));

        // nickNameList에서 랜덤으로 nickName 가져오기
        Random random = new Random();
        int num1 = random.nextInt(nickNameList.size());
        int num2 = random.nextInt(nickNameList2.size());
        return nickNameList.get(num1) + " " + nickNameList2.get(num2);
    }

    public String getImg(String type) {
        List<GameResource> gameResourceList = gameResourceRepository.findAllByType(type);

        Random random = new Random();
        int num = random.nextInt(4);
        return gameResourceList.get(num).getUrl();
    }

}
