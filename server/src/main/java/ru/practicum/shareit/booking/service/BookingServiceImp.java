package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessIsDeniedException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Booking addBooking(Long bookerId, BookItemRequestDto bcd) {

        User booker = userService.getUser(bookerId);
        Item item = itemRepository.findById(bcd.getItemId()).orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getAvailable() == false) {
            throw new ItemNotAvailableException("This item is not available for booking");
        }

        if (booker.getId().equals(item.getUser().getId())) {
            throw new NotFoundException("You can't book your items");
        }

        Booking booking = BookingMapper.toBookingFromCreation(bcd, booker, item);
        booking = bookingRepository.save(booking);

        return booking;
    }


    @Override
    public Booking setAppove(Long userId, Long bookingId, Boolean approved) {
        Optional<Booking> booking1 = bookingRepository.findById(bookingId);
        Booking booking = booking1.orElseThrow(() -> new NotFoundException("Booking has been not found "));
        boolean isOwner = booking.getItem().getUser().getId().equals(userId);
        if (!isOwner) {
            throw new AccessIsDeniedException("Not your item");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ItemNotAvailableException("Item already APPROVED");
        }
        Status newStatus = approved ? Status.APPROVED : Status.REJECTED;
        booking.setStatus(newStatus);
        booking = bookingRepository.save(booking);

        return booking;
    }

    @Override
    public Booking getBookingOfBooker(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if ((!userId.equals(booking.getBooker().getId())) && (!userId.equals(booking.getItem().getUser().getId()))) {
            throw new AccessIsDeniedException("You can't see item with id " + bookingId);
        }

        return booking;
    }

    @Override
    public List<Booking> getAllBookingOfBooker(Long bookerId,
                                               BookingState state,
                                               Integer from,
                                               Integer size) {
        userService.getUser(bookerId);
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Order.desc("start"), Sort.Order.asc("id")));
        List<Booking> result;
        switch (state) {

            case ALL:
                result = bookingRepository.getAllBookingOfBooker(bookerId, pageable);
                break;
            case PAST:
                result = bookingRepository.getPastBookingOfBooker(bookerId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                result = bookingRepository.getFutureBookingOfBooker(bookerId, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                result = bookingRepository.getCurrentBookingOfBooker(bookerId, LocalDateTime.now(), pageable);
                break;
            default:
                result = bookingRepository
                        .getRejectedOrWaitingBookingOfBooker(bookerId, Status.valueOf(state.toString()), pageable);
                break;


        }
        return result;
    }

    @Override
    public List<Booking> getBookingOfOwner(Long bookerId,
                                           BookingState state,
                                           Integer from,
                                           Integer size) {
        User booker = userService.getUser(bookerId);
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Order.desc("start"), Sort.Order.asc("id")));
        List<Booking> result;
        switch (state) {

            case ALL:
                result = bookingRepository.getAllBookingOfOwner(bookerId, pageable);
                break;
            case PAST:
                result = bookingRepository.getPastBookingOfOwner(bookerId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                result = bookingRepository.getFutureBookingOfOwner(bookerId, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                result = bookingRepository.getCurrentBookingOfOwner(bookerId, LocalDateTime.now(), pageable);
                break;
            default:
                result = bookingRepository.getRejectedOrWaitingBookingOfOwner(bookerId,
                        Status.valueOf(state.toString()),
                        pageable);
                break;


        }
        return result;
    }


}
