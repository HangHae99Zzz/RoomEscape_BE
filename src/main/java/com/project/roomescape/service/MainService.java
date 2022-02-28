package com.project.roomescape.service;

import com.project.roomescape.model.Quiz;
import com.project.roomescape.model.Room;
import com.project.roomescape.repository.QuizRepository;
import com.project.roomescape.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Service
public class MainService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String REDIRECT = "redirect:/";

    private final RoomRepository roomRepository;
    private final QuizRepository quizRepository;

    @Autowired
    public MainService(final RoomRepository roomRepository, final QuizRepository quizRepository) {
        this.roomRepository = roomRepository;
        this.quizRepository = quizRepository;
    }

    public String displayMainPage(final String teamName, final String uuid, Model model) {
        model.addAttribute("id", teamName);
        model.addAttribute("rooms", roomRepository.findAll());
        //       UUID 유저 아이디를 의미. 이 값은 main.js에서 만들어진다.

        model.addAttribute("uuid", uuid);
        return "main";
    }

    public String processRoomSelection(final String teamName, final String uuid, final BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            // simplified version, no errors processing
//            @modelAttribute 로 값 가져오기 실패시 홈으로 redirect.
            throw new IllegalArgumentException("잘못된 방생성입니다.");
        }
        Optional<Room> teamRoom = roomRepository.findByTeamName(teamName);

        if(teamRoom.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 teamName입니다.");
        }
        else {
            Quiz quiz = new Quiz("temp", "temp", "temp", "temp");
            Quiz tempQuiz = quizRepository.save(quiz);
            Room room= new Room(tempQuiz, 3L, teamName);
            roomRepository.save(room);
        }
        return displayMainPage(teamName, uuid, model);
    }

    public String displaySelectedRoom(final String teamName, final String uuid, Model model) {
        // redirect to main page if provided data is invalid
        // (이해안감, 뜻은 teamName이랑 uuid가 유효하지 않으면 메인페이지로 리다이렉트.)
//        ModelAndView modelAndView = new ModelAndView(REDIRECT);

            Optional<Room> room = roomRepository.findByTeamName(teamName);
//            전부 다 정상이라면
            if(room.isPresent() && uuid != null && !uuid.isEmpty()) {
//                로그 기록을 띄웁니다. {}에 uuid, teamName가 들어간다.
                logger.debug("User {} is going to join Room #{}", uuid, teamName);
                // open the chat room
//
                model.addAttribute("uuid", uuid);
                model.addAttribute("id", teamName);

            } else {
                throw new IllegalArgumentException("잘못된 접근입니다!");
            }

        return "chat_room";
    }

    public String processRoomExit(final String teamName, final String uuid, Model model) {
        if(teamName != null && uuid != null) {
            logger.debug("User {} has left Room #{}", uuid, teamName);
            // implement any logic you need
        }
//        메인페이지 띄우기.
        return displayMainPage(teamName, uuid, model);
    }



}
