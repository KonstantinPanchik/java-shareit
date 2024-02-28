package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(ItemRequest itemRequest, Long userId);

    List<ItemRequestDto> getRequests(Long userId);

    ItemRequestDto getRequest(Long requestId);

    List<ItemRequestDto> getAllRequests(Long from, Long size);
}
