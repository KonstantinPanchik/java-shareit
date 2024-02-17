package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override

    public ItemResponseDto addItem(ItemCreationDto itemCreationDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User"));
        Item item = ItemMapper.fromCreatingDto(itemCreationDto);
        item.setUser(user);
        item.setComments(new ArrayList<Comment>());
        itemRepository.save(item);
        return ItemMapper.toResponseDto(item);
    }

    @Override
    public ItemResponseDto updateItem(ItemCreationDto itemCreationDto, Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item"));
        if (item.getUser().getId() != userId) {
            throw new AccessIsDeniedException("You can't update this item");
        }
        item = ItemMapper.updateNotNullFromDto(itemCreationDto, item);
        itemRepository.save(item);

        Booking last = bookingRepository.
                findLastByItem(itemId, LocalDateTime.now(), Status.APPROVED).stream().findFirst().orElse(null);
        ItemResponseDto.BookingDto lastDto = BookingMapper.toItemBookingDto(last);

        Booking next = bookingRepository.
                findNextByItem(itemId, LocalDateTime.now(), Status.APPROVED).stream().findFirst().orElse(null);
        ItemResponseDto.BookingDto nextDto = BookingMapper.toItemBookingDto(next);


        return ItemMapper.toResponseDto(item, lastDto, nextDto);
    }

    @Override
    public ItemResponseDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item"));

        Booking last = null;
        Booking next = null;
        if (item.getUser().getId().equals(userId)) {
            last = bookingRepository.
                    findLastByItem(itemId, LocalDateTime.now(), Status.APPROVED).stream().findFirst().orElse(null);

            next = bookingRepository.
                    findNextByItem(itemId, LocalDateTime.now(), Status.APPROVED).stream().findFirst().orElse(null);
        }

        return ItemMapper.toResponseDto(item,
                BookingMapper.toItemBookingDto(last),
                BookingMapper.toItemBookingDto(next));
    }

    @Override
    public List<ItemResponseDto> getUserItems(Long userId) {
        return itemRepository.userItems(userId).stream().
                map(item -> {
                    Booking last = bookingRepository.
                            findLastByItem(item.getId(), LocalDateTime.now(), Status.APPROVED).stream().findFirst().orElse(null);
                    ItemResponseDto.BookingDto lastDto = BookingMapper.toItemBookingDto(last);

                    Booking next = bookingRepository.
                            findNextByItem(item.getId(), LocalDateTime.now(), Status.APPROVED).stream().findFirst().orElse(null);
                    ItemResponseDto.BookingDto nextDto = BookingMapper.toItemBookingDto(next);


                    return ItemMapper.toResponseDto(item, lastDto, nextDto);

                }).
                collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> search(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream().
                map(ItemMapper::toResponseDto).
                collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto.CommentDto addComment(Comment comment, Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User"));
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
}
