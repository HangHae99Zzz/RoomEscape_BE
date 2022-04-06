package com.project.roomescape.service;

import com.project.roomescape.model.Comment;
import com.project.roomescape.repository.CommentRepository;
import com.project.roomescape.requestDto.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    // 코멘트 입력하기
    public void createComment(CommentRequestDto commentRequestDto) {
        // comment 인스턴스에 requestDto로 받은 comment를 담아준다
        Comment comment = new Comment(commentRequestDto.getComment());
        // repository에 저장해준다
        commentRepository.save(comment);
    }

}
