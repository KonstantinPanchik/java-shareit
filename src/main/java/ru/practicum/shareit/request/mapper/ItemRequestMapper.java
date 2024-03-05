package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.stream.Collectors;

public class ItemRequestMapper {

    private static ItemRequestDto.ItemDto toItemDtoForRequest(Item item) {
        return ItemRequestDto.ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .requestId(item.getRequest().getId())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {


        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems()
                        .stream()
                        .map(ItemRequestMapper::toItemDtoForRequest)
                        .collect(Collectors.toList()))
                .build();

    }
}
