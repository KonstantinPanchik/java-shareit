package ru.practicum.shareit.itemTest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessIsDeniedException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemServiceTest {
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    UserService userService;
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    ItemRequestRepository requestRepository;

    @Autowired
    ItemService service;

    @Test
    public void shouldAddItem() {
        when(userService.getUser(anyLong()))
                .thenReturn(testUser());

        when(itemRepository.save(any()))
                .thenAnswer(invocationOnMock -> {
                    Item item = invocationOnMock.getArgument(0);
                    item.setId(10L);
                    return item;
                });
        ItemCreationDto itemCreationDto = ItemCreationDto.builder()
                .name("item")
                .description("itemDescription")
                .available(true)
                .build();
        ItemResponseDto itemResponseDto = service.addItem(itemCreationDto, 1L);

        assertEquals(itemResponseDto.getName(), itemCreationDto.getName());
        assertEquals(itemResponseDto.getDescription(), itemCreationDto.getDescription());
        assertNull(itemResponseDto.getLastBooking());
        assertNull(itemResponseDto.getNextBooking());
        assertNull(itemResponseDto.getRequestId());
    }

    @Test
    public void shouldAddItemWithRequest() {
        when(userService.getUser(anyLong()))
                .thenReturn(testUser());

        when(itemRepository.save(any()))
                .thenAnswer(invocationOnMock -> {
                    Item item = invocationOnMock.getArgument(0);
                    item.setId(10L);
                    return item;
                });

        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(testRequest()));


        ItemCreationDto itemCreationDto = ItemCreationDto.builder()
                .name("item")
                .description("itemDescription")
                .available(true)
                .requestId(10L)
                .build();
        ItemResponseDto itemResponseDto = service.addItem(itemCreationDto, 1L);

        assertEquals(itemResponseDto.getName(), itemCreationDto.getName());
        assertEquals(itemResponseDto.getDescription(), itemCreationDto.getDescription());
        assertNull(itemResponseDto.getLastBooking());
        assertNull(itemResponseDto.getNextBooking());
        assertNotNull(itemResponseDto.getRequestId());
    }

    @Test
    public void shouldUpdateName() {
        ItemCreationDto dto = ItemCreationDto.builder().name("newName").build();

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem(testUser())));

        when(bookingRepository.findAllByItemAndAndStatus(any(), any()))
                .thenReturn(new ArrayList<>());


        ItemResponseDto responseDto = service.updateItem(dto, 1L, 2L);
        assertEquals(responseDto.getName(), dto.getName());
        assertEquals(responseDto.getDescription(), "OldDescription");

    }


    @Test
    public void shouldUpdateDescription() {
        ItemCreationDto dto = ItemCreationDto.builder().description("newDescription").build();

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem(testUser())));

        when(bookingRepository.findAllByItemAndAndStatus(any(), any()))
                .thenReturn(new ArrayList<>());

        ItemResponseDto responseDto = service.updateItem(dto, 1L, 2L);
        assertEquals(responseDto.getDescription(), dto.getDescription());

    }

    @Test
    public void shouldUpdateAvailable() {
        ItemCreationDto dto = ItemCreationDto.builder().available(true).build();

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem(testUser())));

        when(bookingRepository.findAllByItemAndAndStatus(any(), any()))
                .thenReturn(new ArrayList<>());

        ItemResponseDto responseDto = service.updateItem(dto, 1L, 2L);
        assertEquals(responseDto.getAvailable(), dto.getAvailable());

    }

    @Test
    public void shouldNotUpdateName() {
        ItemCreationDto dto = ItemCreationDto.builder().name("newName").build();

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem(testUser())));

        when(bookingRepository.findAllByItemAndAndStatus(any(), any()))
                .thenReturn(new ArrayList<>());

        assertThrows(AccessIsDeniedException.class, () -> service.updateItem(dto, 2L, 2L));


    }

    @Test
    public void shouldGetItem() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem(testUser())));

        when(bookingRepository.findAllByItemAndAndStatus(any(), any()))
                .thenReturn(List.of(testLastBooking(new User(31L, "user31", "user31@mail.ru")),
                        testNextBooking(new User(41L, "user41", "user41@mail.ru"))));


        ItemResponseDto responseDto = service.getItem(2L, 1L);

        verify(bookingRepository, times(1)).findAllByItemAndAndStatus(any(), any());

        assertEquals(responseDto.getName(), testItem(testUser()).getName());
        assertNotNull(responseDto.getNextBooking());
        assertNotNull(responseDto.getLastBooking());

    }

    @Test
    public void shouldGetItemWithOutBookings() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem(testUser())));

        when(bookingRepository.findAllByItemAndAndStatus(any(), any()))
                .thenReturn(List.of(testLastBooking(new User(31L, "user31", "user31@mail.ru")),
                        testNextBooking(new User(41L, "user41", "user41@mail.ru"))));


        ItemResponseDto responseDto = service.getItem(2L, 1341L);

        verify(bookingRepository, times(0)).findAllByItemAndAndStatus(any(), any());

        assertEquals(responseDto.getName(), testItem(testUser()).getName());
        assertNull(responseDto.getNextBooking());
        assertNull(responseDto.getLastBooking());


    }

    @Test
    public void shouldNotAddComment() {
        when(userService.getUser(anyLong()))
                .thenReturn(testUser());

        when(bookingRepository.findByItemAndBooker(anyLong(), anyLong(), any()))
                .thenReturn(new ArrayList<>());

        CommentCreationDto comment = new CommentCreationDto();
        comment.setText("text");

        assertThrows(ItemNotAvailableException.class, () -> service.addComment(comment, 2L, 3L));


    }

    @Test
    public void shouldAddComment() {
        when(userService.getUser(anyLong()))
                .thenReturn(testUser());

        when(bookingRepository.findByItemAndBooker(anyLong(), anyLong(), any()))
                .thenReturn(List.of(new Booking()));

        when(commentRepository.save(any()))
                .thenAnswer(invocationOnMock -> {
                    Comment comment = invocationOnMock.getArgument(0);
                    comment.setId(88L);
                    return comment;
                });

        CommentCreationDto comment = new CommentCreationDto();
        comment.setText("text");

        ItemResponseDto.CommentDto result = service.addComment(comment, testUser().getId(), 3L);
        assertEquals(result.getText(), comment.getText());

    }


    @Test
    public void shouldGetItems() {

        when(itemRepository.userItems(anyLong(), any()))
                .thenReturn(List.of(
                        Item.builder()
                                .id(1L)
                                .comments(new ArrayList<>())
                                .build(),
                        Item.builder()
                                .id(2L)
                                .comments(new ArrayList<>())
                                .build()

                ));

        List<ItemResponseDto> result = service.getUserItems(1L, 0, 1);
        assertEquals(result.get(0).getId(), 1L);
        assertEquals(result.get(1).getId(), 2L);
    }

    @Test
    public void shouldGetItemsSearch() {

        when(itemRepository.search(any(), any()))
                .thenReturn(List.of(
                        Item.builder()
                                .id(1L)
                                .comments(new ArrayList<>())
                                .build(),
                        Item.builder()
                                .id(2L)
                                .comments(new ArrayList<>())
                                .build()

                ));

        List<ItemResponseDto> result = service.search("text", 0, 1);
        assertEquals(result.get(0).getId(), 1L);
        assertEquals(result.get(1).getId(), 2L);
    }

    @Test
    public void shouldEmptyItemsSearch() {

        List<ItemResponseDto> resultTextNull = service.search(null, 0, 1);
        List<ItemResponseDto> resultTextBlank = service.search("", 0, 1);

        assertTrue(resultTextNull.isEmpty());
        assertTrue(resultTextBlank.isEmpty());
    }

    private User testUser() {
        return new User(1L, "name", "emai@mail.ru");
    }

    private Item testItem(User user) {
        return Item.builder()
                .id(2L)
                .name("OldName")
                .description("OldDescription")
                .comments(new ArrayList<>())
                .user(user)
                .available(false)
                .request(null)
                .build();

    }

    private ItemRequest testRequest() {
        return ItemRequest.builder()
                .id(10L)
                .created(LocalDateTime.now())
                .description("need item")
                .build();
    }

    private Booking testNextBooking(User user) {
        return Booking.builder()
                .id(4L)
                .booker(user)
                .item(testItem(testUser()))
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

    }

    private Booking testLastBooking(User user) {
        return Booking.builder()
                .id(3L)
                .booker(user)
                .item(testItem(testUser()))
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();
    }
}
