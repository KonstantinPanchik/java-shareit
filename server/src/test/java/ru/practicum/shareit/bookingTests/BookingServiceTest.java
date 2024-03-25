package ru.practicum.shareit.bookingTests;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessIsDeniedException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BookingServiceTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;

    @Autowired
    ItemRepository itemRepository;

    static User user1;
    static User user2;

    static Item item1;
    static Item item2;

    @BeforeEach
    public void setUserAndItemsInDB() {

        user1 = new User(null, "user1", "userBookTest1@mail.ru");
        user2 = new User(null, "user2", "userBookTest2@mail.ru");

        userService.addUser(user1);
        userService.addUser(user2);

        item1 = Item.builder()
                .name("item1")
                .user(user1)
                .description("item1 description")
                .available(true)
                .build();

        item2 = Item.builder()
                .name("item2")
                .user(user1)
                .description("item2 description")
                .available(false)
                .build();

        itemRepository.save(item1);
        itemRepository.save(item2);

    }

    @AfterEach
    public void deleteData() {

        itemRepository.deleteById(item1.getId());
        itemRepository.deleteById(item2.getId());

        userService.deleteUser(user1.getId());
        userService.deleteUser(user2.getId());

    }

    @Test
    public void shouldAddBooking() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookItemRequestDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking booking = bookingService.addBooking(user2.getId(), bookItemRequestDto);

        User booker = booking.getBooker();
        assertEquals(booker, user2);
        assertEquals(booking.getItem().getId(), item1.getId());
        assertEquals(booking.getStatus(), Status.WAITING);

    }

    @Test
    public void shouldNodAddBookingMyItem() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookItemRequestDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(user1.getId(), bookItemRequestDto));


    }

    @Test
    public void shouldNodAddBookingNotAvailable() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item2.getId());
        bookItemRequestDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookItemRequestDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        assertThrows(ItemNotAvailableException.class, () -> bookingService.addBooking(user1.getId(), bookItemRequestDto));


    }

    @Test
    public void shouldSetApproveBooking() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookItemRequestDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking booking = bookingService.addBooking(user2.getId(), bookItemRequestDto);
        assertEquals(booking.getStatus(), Status.WAITING);
        Booking appovedBooking = bookingService.setAppove(user1.getId(), booking.getId(), true);
        assertEquals(appovedBooking.getStatus(), Status.APPROVED);


    }

    @Test
    public void shouldSetRejectedBooking() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookItemRequestDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking booking = bookingService.addBooking(user2.getId(), bookItemRequestDto);
        assertEquals(booking.getStatus(), Status.WAITING);
        Booking appovedBooking = bookingService.setAppove(user1.getId(), booking.getId(), false);
        assertEquals(appovedBooking.getStatus(), Status.REJECTED);


    }

    @Test
    public void shouldExceptionNotOwnerBooking() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookItemRequestDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));
        Booking booking = bookingService.addBooking(user2.getId(), bookItemRequestDto);
        assertEquals(booking.getStatus(), Status.WAITING);
        assertThrows(AccessIsDeniedException.class, () -> bookingService.setAppove(user2.getId(), booking.getId(), true));
    }

    @Test
    public void shouldExceptionAlreadyApproved() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookItemRequestDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking booking = bookingService.addBooking(user2.getId(), bookItemRequestDto);
        assertEquals(booking.getStatus(), Status.WAITING);
        Booking appovedBooking = bookingService.setAppove(user1.getId(), booking.getId(), true);
        assertEquals(appovedBooking.getStatus(), Status.APPROVED);
        assertThrows(ItemNotAvailableException.class, () -> bookingService.setAppove(user1.getId(), booking.getId(), true));

    }

    @Test
    public void shouldGetBooking() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookItemRequestDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking savedbooking = bookingService.addBooking(user2.getId(), bookItemRequestDto);

        Booking gotByItemOwner = bookingService.getBookingOfBooker(user1.getId(), savedbooking.getId());

        Booking gotByBooker = bookingService.getBookingOfBooker(user2.getId(), savedbooking.getId());

        assertEquals(savedbooking, gotByItemOwner);
        assertEquals(savedbooking, gotByBooker);


        User user3 = new User(null, "user3", "userBookTest3@mail.ru");
        userService.addUser(user3);

        assertThrows(AccessIsDeniedException.class,
                () -> bookingService.getBookingOfBooker(user3.getId(), savedbooking.getId()));
    }

    @Test
    public void shouldGetAllBookings() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().plusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().plusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getAllBookingOfBooker(user2.getId(),
                BookingState.ALL,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 2);
    }

    @Test
    public void shouldGetFutureBookings() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().plusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().plusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getAllBookingOfBooker(user2.getId(),
                BookingState.FUTURE,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 2);


    }

    @Test
    public void shouldGetPastBookings() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookItemRequestDto.setEnd(LocalDateTime.now().minusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().minusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().minusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getAllBookingOfBooker(user2.getId(),
                BookingState.PAST,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 2);


    }

    @Test
    public void shouldGetCurrentBookings() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().minusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().minusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getAllBookingOfBooker(user2.getId(),
                BookingState.CURRENT,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 1);
    }

    @Test
    public void shouldGetWaitingBookings() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().minusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().minusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getAllBookingOfBooker(user2.getId(),
                BookingState.WAITING,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 2);
    }

    @Test
    public void shouldGetRejectedBookings() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(4));

        Booking booking = bookingService.addBooking(user2.getId(), bookItemRequestDto);
        bookingService.setAppove(user1.getId(), booking.getId(), false);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().minusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().minusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> rejectedBookingsOfBooker = bookingService.getAllBookingOfBooker(user2.getId(),
                BookingState.REJECTED,
                0, 10);

        assertEquals(rejectedBookingsOfBooker.size(), 1);
    }

    @Test
    public void shouldGetAllBookingsOwner() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().plusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().plusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getBookingOfOwner(user1.getId(),
                BookingState.ALL,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 2);
    }

    @Test
    public void shouldGetFutureBookingsOwner() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().plusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().plusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getBookingOfOwner(user1.getId(),
                BookingState.FUTURE,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 2);
    }

    @Test
    public void shouldGetPastBookingsOwner() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookItemRequestDto.setEnd(LocalDateTime.now().minusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().minusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().minusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getBookingOfOwner(user1.getId(),
                BookingState.PAST,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 2);
    }

    @Test
    public void shouldGetCurrentBookingsOwner() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().minusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().minusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getBookingOfOwner(user1.getId(),
                BookingState.CURRENT,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 1);
    }

    @Test
    public void shouldGetWaitingBookingsOwner() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(4));

        bookingService.addBooking(user2.getId(), bookItemRequestDto);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().minusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().minusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> bookingsOfBooker = bookingService.getBookingOfOwner(user1.getId(),
                BookingState.WAITING,
                0, 10);

        assertEquals(bookingsOfBooker.size(), 2);
    }

    @Test
    public void shouldGetRejectedBookingsOwner() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(item1.getId());
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(4));

        Booking booking = bookingService.addBooking(user2.getId(), bookItemRequestDto);
        bookingService.setAppove(user1.getId(), booking.getId(), false);

        BookItemRequestDto bookItemRequestDto2 = new BookItemRequestDto();
        bookItemRequestDto2.setItemId(item1.getId());
        bookItemRequestDto2.setStart(LocalDateTime.now().minusDays(3));
        bookItemRequestDto2.setEnd(LocalDateTime.now().minusDays(2));

        bookingService.addBooking(user2.getId(), bookItemRequestDto2);


        List<Booking> rejectedBookingsOfBooker = bookingService.getBookingOfOwner(user1.getId(),
                BookingState.REJECTED,
                0, 10);

        assertEquals(rejectedBookingsOfBooker.size(), 1);
    }
}
