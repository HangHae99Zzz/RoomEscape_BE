package com.project.roomescape.controller;

import com.project.roomescape.requestDto.CommentRequestDto;
import com.project.roomescape.responseDto.CommentResponseDto;
import com.project.roomescape.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    // 코멘트 입력하기
    @PostMapping("/comments")
    public void createComment(@RequestBody CommentRequestDto commentRequestDto){
        commentService.createComment(commentRequestDto);
    }

    // 코멘트 조회하기
    @GetMapping("/comments")
    public List<CommentResponseDto> getComments() {
        return commentService.getComments();
    }

}
