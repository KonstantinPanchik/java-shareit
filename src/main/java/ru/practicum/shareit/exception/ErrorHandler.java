package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFound(final NotFoundException e) {
        log.error(e.getMessage());
        ResponseEntity<Map<String, String>> user = new ResponseEntity<>(
                Map.of("User", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
        return user;
    }

    @ExceptionHandler
    public ResponseEntity handleEmailAlreadyExist(final EmailAlreadyExistException e) {
        log.error(e.getMessage());
        ResponseEntity email = ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        return email;
    }

    @ExceptionHandler
    public ResponseEntity handleAccessIsDenied(final AccessIsDeniedException e) {
        log.error(e.getMessage());
        ResponseEntity access = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        return access;
    }

    @ExceptionHandler
    public ResponseEntity handleAccessIsDenied(final MethodArgumentNotValidException e) {
        log.error("Validation Exception");
        ResponseEntity access = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        return access;
    }

    @ExceptionHandler
    public ResponseEntity handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        ResponseEntity ex = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        return ex;
    }

}