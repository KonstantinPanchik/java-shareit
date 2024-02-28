package ru.practicum.shareit.exception;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException() {
        super();
    }

    public ItemNotAvailableException(String message) {
        super(message);
    }
}