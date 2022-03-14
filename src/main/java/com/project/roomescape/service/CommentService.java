package com.project.roomescape.service;

import com.project.roomescape.model.Comment;
import com.project.roomescape.repository.CommentRepository;
import com.project.roomescape.requestDto.CommentRequestDto;
import com.project.roomescape.responseDto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    // 코멘트 입력하기
    public void createComment(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto.getComment());
        commentRepository.save(comment);
    }

    // 코멘트 조회하기
    public List<CommentResponseDto> getComments() {
        List<Comment> commentList = commentRepository.findAll();
        List<CommentResponseDto> commentDtoList = new ArrayList<>();
        for(Comment comment: commentList) {
            commentDtoList.add(new CommentResponseDto(
                    comment.getCreatedAt(), comment.getComment()));
        }
        return commentDtoList;
    }
}
