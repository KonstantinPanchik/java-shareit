package ru.practicum.shareit.bookingTests;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessIsDeniedException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

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
        BookingCreationDto bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(item1.getId());
        bookingCreationDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookingCreationDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking booking = bookingService.addBooking(user2.getId(), bookingCreationDto);

        User booker = booking.getBooker();
        assertEquals(booker, user2);
        assertEquals(booking.getItem().getId(), item1.getId());
        assertEquals(booking.getStatus(), Status.WAITING);

    }

    @Test
    public void shouldNodAddBookingMyItem() {
        BookingCreationDto bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(item1.getId());
        bookingCreationDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookingCreationDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(user1.getId(), bookingCreationDto));


    }

    @Test
    public void shouldNodAddBookingNotAvailable() {
        BookingCreationDto bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(item2.getId());
        bookingCreationDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookingCreationDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        assertThrows(ItemNotAvailableException.class, () -> bookingService.addBooking(user1.getId(), bookingCreationDto));


    }

    @Test
    public void shouldSetApproveBooking() {
        BookingCreationDto bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(item1.getId());
        bookingCreationDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookingCreationDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking booking = bookingService.addBooking(user2.getId(), bookingCreationDto);
        assertEquals(booking.getStatus(), Status.WAITING);
        Booking appovedBooking = bookingService.setAppove(user1.getId(), booking.getId(), true);
        assertEquals(appovedBooking.getStatus(), Status.APPROVED);


    }

    @Test
    public void shouldSetRejectedBooking() {
        BookingCreationDto bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(item1.getId());
        bookingCreationDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookingCreationDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking booking = bookingService.addBooking(user2.getId(), bookingCreationDto);
        assertEquals(booking.getStatus(), Status.WAITING);
        Booking appovedBooking = bookingService.setAppove(user1.getId(), booking.getId(), false);
        assertEquals(appovedBooking.getStatus(), Status.REJECTED);


    }

    @Test
    public void shouldExceptionNotOwnerBooking() {
        BookingCreationDto bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(item1.getId());
        bookingCreationDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookingCreationDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));
        Booking booking = bookingService.addBooking(user2.getId(), bookingCreationDto);
        assertEquals(booking.getStatus(), Status.WAITING);
        assertThrows(AccessIsDeniedException.class, () -> bookingService.setAppove(user2.getId(), booking.getId(), true));
    }

    @Test
    public void shouldExceptionAlreadyApproved() {
        BookingCreationDto bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(item1.getId());
        bookingCreationDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookingCreationDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking booking = bookingService.addBooking(user2.getId(), bookingCreationDto);
        assertEquals(booking.getStatus(), Status.WAITING);
        Booking appovedBooking = bookingService.setAppove(user1.getId(), booking.getId(), true);
        assertEquals(appovedBooking.getStatus(), Status.APPROVED);
        assertThrows(ItemNotAvailableException.class, () -> bookingService.setAppove(user1.getId(), booking.getId(), true));

    }

    @Test
    public void shouldGetBooking() {
        BookingCreationDto bookingCreationDto = new BookingCreationDto();
        bookingCreationDto.setItemId(item1.getId());
        bookingCreationDto.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        bookingCreationDto.setEnd(LocalDateTime.of(2020, 10, 12, 10, 10));

        Booking savedbooking = bookingService.addBooking(user2.getId(), bookingCreationDto);

        Booking gotByItemOwner = bookingService.getBookingOfBooker(user1.getId(), savedbooking.getId());

        Booking gotByBooker = bookingService.getBookingOfBooker(user2.getId(), savedbooking.getId());

        assertEquals(savedbooking, gotByItemOwner);
        assertEquals(savedbooking, gotByBooker);


        User user3 = new User(null, "user3", "userBookTest3@mail.ru");
        userService.addUser(user3);

        assertThrows(AccessIsDeniedException.class,
                () -> bookingService.getBookingOfBooker(user3.getId(), savedbooking.getId()));


    }
}
