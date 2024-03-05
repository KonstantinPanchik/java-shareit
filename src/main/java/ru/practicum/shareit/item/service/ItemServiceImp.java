package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessIsDeniedException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
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
@Slf4j
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public ItemResponseDto addItem(ItemCreationDto itemCreationDto, Long userId) {
        User user = userService.getUser(userId);
        Item item = ItemMapper.fromCreatingDto(itemCreationDto);
        item.setUser(user);
        item.setComments(new ArrayList<Comment>());

        if (itemCreationDto.getRequestId() != null) {
            ItemRequest itemRequest = requestRepository.findById(itemCreationDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Item Request with id " + itemCreationDto.getRequestId() +
                            " not found"));
            item.setRequest(itemRequest);
        }
        itemRepository.save(item);
        return ItemMapper.toResponseDto(item);
    }

    @Override
    public ItemResponseDto updateItem(ItemCreationDto itemCreationDto, Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item"));
        if (!(item.getUser().getId().equals(userId))) {
            throw new AccessIsDeniedException("You can't update this item");
        }
        item = ItemMapper.updateNotNullFromDto(itemCreationDto, item);
        itemRepository.save(item);

        return setBookings(item, allApprovedBooking(List.of(item)));
    }

    @Override
    public ItemResponseDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item"));

        if (item.getUser().getId().equals(userId)) {

            List<Booking> bookings = allApprovedBooking(List.of(item));

            return setBookings(item, bookings);
        } else {

            return ItemMapper.toResponseDto(item);
        }
    }

    @Override
    public List<ItemResponseDto> getUserItems(Long userId, Integer from, Integer size) {

        List<Item> items = itemRepository.userItems(userId, PageRequest.of(from / size, size, Sort.by("id")));

        List<Booking> bookings = allApprovedBooking(items);

        return items.stream().map(item -> setBookings(item, bookings)).collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> search(String text, Integer from, Integer size) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text, PageRequest.of(from / size, size, Sort.by("id")))
                .stream()
                .map(ItemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto.CommentDto addComment(Comment comment, Long userId, Long itemId) {
        User user = userService.getUser(userId);
        List<Booking> bookings = bookingRepository.findByItemAndBooker(itemId, userId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ItemNotAvailableException("You didn't book this item");
        }
        comment.setItemId(itemId);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return ItemMapper.toCommentDto(comment);

    }

    private ItemResponseDto setBookings(Item item, List<Booking> allApproved) {


        Booking last = allApproved
                .stream()
                .filter(booking -> booking.getItem().getId().equals(item.getId()))
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .sorted((o1, o2) -> {
                    if (o1.getStart().isBefore(o2.getStart())) {
                        return 1;
                    } else {
                        return -1;
                    }
                })
                .findFirst().orElse(null);
        ItemResponseDto.BookingDto lastDto = BookingMapper.toItemBookingDto(last);

        Booking next = allApproved
                .stream()
                .filter(booking -> booking.getItem().getId().equals(item.getId()))
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .sorted((o1, o2) -> {
                    if (o1.getStart().isBefore(o2.getStart())) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .findFirst().orElse(null);
        ItemResponseDto.BookingDto nextDto = BookingMapper.toItemBookingDto(next);


        return ItemMapper.toResponseDto(item, lastDto, nextDto);
    }

    private List<Booking> allApprovedBooking(List<Item> list) {
        return bookingRepository.findAllByItemAndAndStatus(list, Status.APPROVED);
    }
}
