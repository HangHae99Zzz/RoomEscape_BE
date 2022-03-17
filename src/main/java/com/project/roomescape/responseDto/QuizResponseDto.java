package com.project.roomescape.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QuizResponseDto {
    private String question;
    private String content;
    private String hint;
    private String chance;
    private String ingUrl;
    private String answer;

}
