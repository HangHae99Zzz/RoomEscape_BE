package com.project.roomescape.controller;

import com.project.roomescape.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
public class MainController {
    private final MainService mainService;

    @Autowired
    public MainController(final MainService mainService) {
        this.mainService = mainService;
    }


    @GetMapping("/")
//    @RequestParam생략(required = false)
    public String displayMainPage(final String teamName, final String uuid, Model model) {
        return mainService.displayMainPage(teamName, uuid, model);
    }

//    . 입력받은 방이름, 유저아이디 등을 Room에 저장하고 mainpage 새롭게 로딩. 방만들기.
    @PostMapping("/room")
//    방id(방이름)이 sid이고, bindingresult는  modelAttribute로 값을 못가져와서 발생하는 에러 처리하기 위함.
    public String processRoomSelection(@ModelAttribute("id") final String teamName, @ModelAttribute("uuid") final String uuid, final BindingResult binding, Model model) {
        return mainService.processRoomSelection(teamName, uuid, binding, model);
    }

    //    방을 선택할시에 실행(참가). chat_room 띄워준다.
    @GetMapping("/room/{sid}/user/{uuid}")
    public String displaySelectedRoom(@PathVariable("sid") final String teamName, @PathVariable("uuid") final String uuid, Model model) {
        return this.mainService.displaySelectedRoom(teamName, uuid, model);
    }

    //    방 나가기
    @GetMapping("/room/{sid}/user/{uuid}/exit")
    public String processRoomExit(@PathVariable("sid") final String teamName, @PathVariable("uuid") final String uuid, Model model) {
        return this.mainService.processRoomExit(teamName, uuid, model);
    }

}
