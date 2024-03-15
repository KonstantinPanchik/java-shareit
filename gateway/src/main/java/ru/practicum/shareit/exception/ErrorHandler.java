package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {




    @ExceptionHandler
    public ResponseEntity handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.trace("Validation Exception");
        ResponseEntity access = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        return access;
    }


    @ExceptionHandler
    public ResponseEntity handleItemNotAvailableException(final IllegalArgumentException e) {
        log.trace(e.getMessage());

        ResponseEntity access = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        return access;
    }

    @ExceptionHandler
    public ResponseEntity handleThrowable(final Throwable e) {
        log.trace(e.getMessage());
        ResponseEntity ex = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        return ex;
    }


}