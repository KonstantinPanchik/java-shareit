package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceTestWithMock {

    @MockBean
    UserService userService;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    BookingRepository repository;

    @Autowired
    BookingService bookingService;


    @Test
    public void shouldGetAllBookingsOfBooker() {
        when(userService.getUser(anyLong()))
                .thenReturn(getBooker());

        when(repository.getAllBookingOfBooker(anyLong(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getAllBookingOfBooker(1L,
                BookingState.ALL,
                0,
                10);
        verify(repository, times(1))
                .getAllBookingOfBooker(anyLong(), any());

    }

    @Test
    public void shouldGetPastBookingsOfBooker() {
        when(userService.getUser(anyLong()))
                .thenReturn(getBooker());

        when(repository.getPastBookingOfBooker(anyLong(), any(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getAllBookingOfBooker(1L,
                BookingState.PAST,
                0,
                10);
        verify(repository, times(1))
                .getPastBookingOfBooker(anyLong(), any(), any());

    }

    @Test
    public void shouldFuturePastBookingsOfBooker() {
        when(userService.getUser(anyLong()))
                .thenReturn(getBooker());

        when(repository.getFutureBookingOfBooker(anyLong(), any(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getAllBookingOfBooker(1L,
                BookingState.FUTURE,
                0,
                10);
        verify(repository, times(1))
                .getFutureBookingOfBooker(anyLong(), any(), any());

    }

    @Test
    public void shouldCurrentPastBookingsOfBooker() {
        when(userService.getUser(anyLong()))
                .thenReturn(getBooker());

        when(repository.getCurrentBookingOfBooker(anyLong(), any(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getAllBookingOfBooker(1L,
                BookingState.CURRENT,
                0,
                10);
        verify(repository, times(1))
                .getCurrentBookingOfBooker(anyLong(), any(), any());

    }

    @Test
    public void shouldDefaultPastBookingsOfBooker() {
        when(userService.getUser(anyLong()))
                .thenReturn(getBooker());

        when(repository.getRejectedOrWaitingBookingOfBooker(anyLong(), any(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getAllBookingOfBooker(1L,
                BookingState.WAITING,
                0,
                10);
        verify(repository, times(1))
                .getRejectedOrWaitingBookingOfBooker(anyLong(), any(), any());

    }


    @Test
    public void shouldGetAllBookingsOfOwner() {
        when(userService.getUser(2L))
                .thenReturn(getOwner());

        when(repository.getAllBookingOfOwner(anyLong(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getBookingOfOwner(2L,
                BookingState.ALL,
                0,
                10);
        verify(repository, times(1))
                .getAllBookingOfOwner(anyLong(), any());

    }

    @Test
    public void shouldGetPastBookingsOfOwner() {
        when(userService.getUser(2L))
                .thenReturn(getOwner());

        when(repository.getPastBookingOfOwner(anyLong(), any(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getBookingOfOwner(2L,
                BookingState.PAST,
                0,
                10);
        verify(repository, times(1))
                .getPastBookingOfOwner(anyLong(), any(), any());

    }

    @Test
    public void shouldFuturePastBookingsOfOwner() {
        when(userService.getUser(2L))
                .thenReturn(getOwner());

        when(repository.getFutureBookingOfOwner(anyLong(), any(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getBookingOfOwner(2L,
                BookingState.FUTURE,
                0,
                10);
        verify(repository, times(1))
                .getFutureBookingOfOwner(anyLong(), any(), any());

    }

    @Test
    public void shouldCurrentPastBookingsOfOwner() {
        when(userService.getUser(2L))
                .thenReturn(getOwner());

        when(repository.getCurrentBookingOfOwner(anyLong(), any(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getBookingOfOwner(1L,
                BookingState.CURRENT,
                0,
                10);
        verify(repository, times(1))
                .getCurrentBookingOfOwner(anyLong(), any(), any());

    }

    @Test
    public void shouldDefaultPastBookingsOfOwner() {
        when(userService.getUser(2L))
                .thenReturn(getOwner());

        when(repository.getRejectedOrWaitingBookingOfOwner(anyLong(), any(), any()))
                .thenReturn(getTestBookings(3));

        bookingService.getBookingOfOwner(2L,
                BookingState.WAITING,
                0,
                10);
        verify(repository, times(1))
                .getRejectedOrWaitingBookingOfOwner(anyLong(), any(), any());

    }


    private User getBooker() {
        return new User(1L, "user1", "userone@icloud.com");
    }

    private User getOwner() {
        return new User(2L, "user1", "userone@icloud.com");
    }

    private List<Booking> getTestBookings(int length) {
        List<Booking> bookings = new ArrayList<>();

        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("item description")
                .user(getOwner())
                .available(true)
                .build();

        for (int i = 1; i <= length; i++) {
            Booking booking = Booking
                    .builder()
                    .start(LocalDateTime.now().minusDays(1))
                    .end(LocalDateTime.now().plusDays(1))
                    .item(item)
                    .booker(getBooker())
                    .status(Status.APPROVED)
                    .build();

        }
        return bookings;
    }
}
