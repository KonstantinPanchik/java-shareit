package ru.practicum.shareit.exception;

public class AccessIsDeniedException extends RuntimeException{
    public AccessIsDeniedException() {
    }

    public AccessIsDeniedException(String message) {
        super(message);
    }
}
