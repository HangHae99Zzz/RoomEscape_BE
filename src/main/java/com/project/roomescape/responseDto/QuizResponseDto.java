package com.project.roomescape.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuizResponseDto {
    private String question;
    private String content;
    private String clue;
    private String hint;
    private String answer;

}
