package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFound(final NotFoundException e) {
        ResponseEntity<Map<String, String>> user = new ResponseEntity<>(
                Map.of("User", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
        return user;
    }

    @ExceptionHandler
    public ResponseEntity handleEmailAlreadyExist(final EmailAlreadyExistException e) {
        ResponseEntity email = ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        return email;
    }

    @ExceptionHandler
    public ResponseEntity handleAccessIsDenied(final AccessIsDeniedException e) {
        ResponseEntity access = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        return access;
    }

}