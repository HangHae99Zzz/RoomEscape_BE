package com.project.roomescape.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //400 Bad Request
    ROOM_MEMBER_FULL(HttpStatus.BAD_REQUEST, "400_1", "방의 인원이 다 찼습니다."),
    USER_DELETE_FAIL(HttpStatus.BAD_REQUEST, "400_2", "방을 만든 사람은 나갈 수 없습니다."),

    //404 Not Found
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "404_1", "방을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_2", "유저를 찾을 수 없습니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "404_3", "quiz를 찾을 수 없습니다."),
    CLUE_NOT_FOUND(HttpStatus.NOT_FOUND, "404_4", "clue를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
