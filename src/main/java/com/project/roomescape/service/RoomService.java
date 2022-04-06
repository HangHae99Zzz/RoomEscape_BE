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
    // 상수 선언 // 최대인원 4명까지
    private final int ROOM_CAPACITY = 4;

    // 방 개설하기
    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {
        // requestDto에서 teamName 가져오기
        String teamName = roomRequestDto.getTeamName();
        // requestDto에서 방장 Id 가져오기
        String userId = roomRequestDto.getUserId();
        // room에 teamName과 방장 Id 저장하기
        Room room = roomRepository.save(new Room(teamName, userId));

        // 랜덤으로 nickName 부여. ex) 낮잠자는 호랑이
        String nickName = createNickName();
        // type 은 userImg (구슬처럼 생긴 4가지 구슬)
        String type = "userImg";
        // 4가지 구슬 중에 랜덤으로 한가지 url을 선택하여 img에 부여
        String img = createImg(type);

        // 방장 User 생성 후 저장하기
        User user = User.createUser(room, nickName, img, userId);
        userRepository.save(user);
        // log
        log.info("roomId : " + room.getId() + "가 개설되었습니다!");
        // 방 url 주소
        String url = "/room/" + room.getId();
        // userResponseDto를 ArrayList로 선언하여 userList 변수에 추가하기
        List<UserResponseDto> userList = new ArrayList<>();
        userList.add(new UserResponseDto(nickName, img, userId));
        //roomResponseDto에 보낼 것들을 다 담아준다 // 방이 생성되면 현재 방에 방장만 있는거니 currentNum:1 이고, 아직 게임시작안했으니 startAt:null로 지정
        RoomResponseDto roomResponseDto = new RoomResponseDto(
                room.getId(), teamName, userId,
                1, url, userList, null);
        //roomResponseDto를 리턴해준다.
        return roomResponseDto;
    }



    // 방 조회하기    // 대기페이지, 게임페이지
    public RoomResponseDto getRoom(Long roomId) {
        // roomId로 해당하는 room을 찾는다 (예외처리)
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));
        // roomId에 해당하는 url을 만든다
        String url = "/room/" + roomId;
        // user for문 돌면서 roomId에 해당하는 userList를 찾는다
        List<UserResponseDto> userList = findUserList(roomId);
        //roomResponseDto에 보낼 것들을 다 담아준다
        RoomResponseDto roomResponseDto = new RoomResponseDto(
                room.getId(), room.getTeamName(), room.getCreatedUser(),
                room.getUserList().size(), url, userList, room.getStartAt());
         //roomResponseDto를 리턴해준다.
        return roomResponseDto;
    }



    // 방 리스트 조회하기 (한 페이지에 4개의 방씩 조회하기)
    public List<RoomResponseDto> getRooms(int page) {
        // 반환할 리스트 선언
        List<RoomResponseDto> roomResponseDtoList = new ArrayList<>();
        //생성일자 기준으로 내림차순으로 정렬할 것이다.
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        //페이지의 4배씩 roomlist를 보내준다.
        int size = page * 4;
        //항상 0페이지를 내려주는데 사이즈만 4개씩 늘리는 방식으로 내려준다.
        Pageable pageable = PageRequest.of(0, size, sort);
        // 종료하지 않은 활동하고 있는 room만을 page형식으로 찾아서 roomList를 만든다
        Page<Room> roomList = roomRepository.findAllByState(pageable, State.ACTIVE);
        // for문을 돌려
        for(Room eachRoom : roomList){
            // 각 룸의 url을 찾는다
            String url = "/room/" + eachRoom.getId();

            // user for문 돌면서 roomId에 해당하는 userList를 찾는다
            List<UserResponseDto> userList = findUserList(eachRoom.getId());
            // roomResponseDto에 해당하는 것들을 담아준다
            RoomResponseDto roomResponseDto = new RoomResponseDto(
                    eachRoom.getId(), eachRoom.getTeamName(), eachRoom.getCreatedUser(),
                    eachRoom.getUserList().size(), url, userList, eachRoom.getStartAt());
            // roomResponseDtoList에 추가해준다
            roomResponseDtoList.add(roomResponseDto);
        }
        // roomResponseDtoList를 반환한다
        return roomResponseDtoList;
    }



    // userList를 찾는 메소드
    public List<UserResponseDto> findUserList(Long roomId) {
        // UserResponseDto를 ArrayList로 선언한다
        List<UserResponseDto> userList = new ArrayList<>();
        // roomId에 해당하는 user를 다 찾는다
        List<User> users = userRepository.findAllByRoomId(roomId);
        // for문을 돌면서
        for(User eachUser : users) {
            // 각 user의 nickname, img, userId를 찾아 userList에 추가해준다
            userList.add(new UserResponseDto(eachUser.getNickName(), eachUser.getImg(), eachUser.getUserId()));
        }
        // userList를 반환한다
        return userList;
    }



    // 방 참여하기
    public void joinRoom(Long roomId, RoomAddRequestDto roomAddRequestDto) {
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
            String nickName = createNickName();

            // nickName 중복확인
            List<String> nickNameList = new ArrayList<>();
            for (User user : userList) {
                nickNameList.add(user.getNickName());
            }
            while (nickNameList.contains(nickName)) {
                nickName = createNickName();
            }

            String type = "userImg";
            String img = createImg(type);
            // img 중복확인
            List<String> imgList = new ArrayList<>();
            for (User user : userList) {
                imgList.add(user.getImg());
            }
            while (imgList.contains(img)) {
                img = createImg(type);
            }

            // user 정보를 해당 room에 추가
            // user 저장
            User user = User.createUser(room, nickName, img, userId);
            userRepository.save(user);

            log.info(userId + "가 " + roomId + "에 참여하였습니다!");
        }
    }


    public String createNickName() {
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

    public String createImg(String type) {
        List<GameResource> gameResourceList = gameResourceRepository.findAllByType(type);

        Random random = new Random();
        int num = random.nextInt(4);
        return gameResourceList.get(num).getUrl();
    }

}
