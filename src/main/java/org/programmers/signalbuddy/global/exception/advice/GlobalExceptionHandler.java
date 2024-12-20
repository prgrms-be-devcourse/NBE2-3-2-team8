package org.programmers.signalbuddy.global.exception.advice;

import org.programmers.signalbuddy.global.exception.BusinessException;
import org.programmers.signalbuddy.global.exception.ErrorCode;
import org.programmers.signalbuddy.global.exception.GlobalErrorCode;
import org.programmers.signalbuddy.global.exception.advice.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode error = e.getErrorCode();
        return ResponseEntity.status(error.getHttpStatus()).body(new ErrorResponse(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e) {
        int code = GlobalErrorCode.BAD_REQUEST.getCode();
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ErrorResponse errorResponse = new ErrorResponse(code, message);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        ErrorResponse errorResponse = new ErrorResponse(GlobalErrorCode.SERVER_ERROR);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
