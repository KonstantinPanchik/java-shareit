package ru.practicum.shareit.bookingTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {


    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService service;

    @Autowired
    MockMvc mockMvc;

    private static LocalDateTime date = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

    @Test
    public void addBooking() throws Exception {
        BookingCreationDto bookingCreationDto = BookingCreationDto
                .builder()
                .itemId(1L)
                .start(date.plusDays(1L))
                .end(date.plusDays(2))
                .build();
        Booking booking = getTestBooking();
        when(service.addBooking(anyLong(), any()))
                .thenReturn(booking);


        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingCreationDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingCreationDto.getEnd().toString())));

    }

    @Test
    public void getBooking() throws Exception {
        BookingCreationDto bookingCreationDto = BookingCreationDto
                .builder()
                .itemId(1L)
                .start(date.plusDays(1L))
                .end(date.plusDays(2))
                .build();
        Booking booking = getTestBooking();
        when(service.getBookingOfBooker(anyLong(), anyLong()))
                .thenReturn(booking);


        mockMvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().toString())))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toString())));

    }

    @Test
    public void patchApproveBooking() throws Exception {

        Booking booking = getApprovedTestBooking();
        when(service.setAppove(anyLong(), anyLong(), any()))
                .thenReturn(booking);


        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().toString())))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    public void getBookerBooking() throws Exception {

        List<Booking> bookerBookings = bookings(3);
        when(service.getAllBookingOfBooker(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookerBookings);


        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

    }

    @Test
    public void getBookerOwner() throws Exception {

        List<Booking> bookerBookings = bookings(5);
        when(service.getBookingOfOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookerBookings);


        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

    }

    private List<Booking> bookings(int length) {
        User booker = new User(1L, "vitya", "vitya@mail.ru");
        User owner = new User(2L, "vova", "vova@mail.ru");

        List<Booking> result = new ArrayList<>();

        Item item1 = Item.builder()
                .id(1L)
                .name("item1")
                .user(owner)
                .description("item1 description")
                .available(true)
                .comments(new ArrayList<>())
                .build();
        for (int i = 1; i <= length; i++) {

            Booking booking = Booking.builder()
                    .id((long) i)
                    .booker(booker)
                    .item(item1)
                    .status(Status.WAITING)
                    .start(date.plusDays(1 + i))
                    .end(date.plusDays(2 + i))
                    .build();
            result.add(booking);
        }
        return result;
    }


    private Booking getApprovedTestBooking() {
        Booking booking = getTestBooking();
        booking.setStatus(Status.APPROVED);
        return booking;
    }

    private Booking getTestBooking() {
        User booker = new User(1L, "vitya", "vitya@mail.ru");
        User owner = new User(2L, "vova", "vova@mail.ru");

        Item item1 = Item.builder()
                .id(1L)
                .name("item1")
                .user(owner)
                .description("item1 description")
                .available(true)
                .comments(new ArrayList<>())
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .booker(booker)
                .item(item1)
                .status(Status.WAITING)
                .start(date.plusDays(1))
                .end(date.plusDays(2))
                .build();

        return booking;


    }

}
