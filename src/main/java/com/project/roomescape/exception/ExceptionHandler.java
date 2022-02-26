package com.project.roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {CustomException.class})
    public ResponseEntity handleCustomException(CustomException ex) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setErrorCode(ex.getErrorCode().getErrorCode());
        errorResult.setHttpStatus(ex.getErrorCode().getHttpStatus());
        errorResult.setErrorMessage(ex.getErrorCode().getErrorMessage());

        return new ResponseEntity(errorResult, errorResult.getHttpStatus());
    }
}