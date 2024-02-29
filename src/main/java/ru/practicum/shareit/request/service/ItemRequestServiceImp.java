package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImp implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final UserService userService;

    @Override
    public ItemRequestDto addRequest(ItemRequest itemRequest, Long userId) {
        itemRequest.setRequestor(userService.getUser(userId));
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setItems(new ArrayList<>());
        itemRequest = itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequests(Long userId) {
        User user = userService.getUser(userId);

        return itemRequestRepository.findItemRequestByRequestorOrderByCreatedDesc(user)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(Long requestId, Long userId) {
        userService.getUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Item request with id: " + requestId + " not found"));


        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer from, Integer size, Long userId) {

        userService.getUser(userId);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());

        List<ItemRequest> result = itemRequestRepository.findAll(pageable).getContent();

        return result.stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }
}
