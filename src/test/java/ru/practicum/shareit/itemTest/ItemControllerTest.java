package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    // TODO: 03.03.2024  update, search, getall, comment


    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService service;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void shouldAddItem() throws Exception {

        ItemCreationDto itemCreationDto = ItemCreationDto
                .builder()
                .requestId(10L)
                .name("name")
                .description("description")
                .available(true)
                .build();

        when(service.addItem(any(), anyLong()))
                .thenReturn(testDto(1L));


        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreationDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(10)))
                .andExpect(jsonPath("$.nextBooking").exists())
                .andExpect(jsonPath("$.lastBooking").exists())
                .andExpect(jsonPath("$.comments").isNotEmpty());


    }

    @Test
    public void shouldGetItem() throws Exception {


        when(service.getItem(anyLong(), anyLong()))
                .thenReturn(testDto(1L));


        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(10)))
                .andExpect(jsonPath("$.nextBooking").exists())
                .andExpect(jsonPath("$.lastBooking").exists())
                .andExpect(jsonPath("$.comments").isNotEmpty());


    }

    @Test
    public void shouldUpdateItem() throws Exception {
        ItemCreationDto dto = ItemCreationDto.builder().description("бла бла").build();

        when(service.updateItem(any(), anyLong(), anyLong()))
                .thenReturn(testDto(1L));


        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(10)))
                .andExpect(jsonPath("$.nextBooking").exists())
                .andExpect(jsonPath("$.lastBooking").exists())
                .andExpect(jsonPath("$.comments").isNotEmpty());


    }

    @Test
    public void shouldGetItems() throws Exception {


        when(service.getUserItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(testDtos(3));


        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)));


    }

    @Test
    public void shouldSearchItems() throws Exception {


        when(service.search(any(), anyInt(), anyInt()))
                .thenReturn(testDtos(3));


        mockMvc.perform(get("/items/search?text=any")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)));


    }

    @Test
    public void shouldAddComment() throws Exception {
        Comment comment = new Comment();
        comment.setText("sdfsdfdfsfsfdsf");

        when(service.addComment(any(), anyLong(), anyLong()))
                .thenReturn(testCommentDto());


        mockMvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is(testCommentDto().getAuthorName())));


    }

    private ItemResponseDto.CommentDto testCommentDto() {

        return ItemResponseDto.CommentDto.builder()
                .authorName("name_user")
                .created(LocalDateTime.now().minusDays(2))
                .text("some text")
                .build();
    }


    private List<ItemResponseDto> testDtos(int length) {
        List<ItemResponseDto> result = new ArrayList<>();
        for (int i = 1; i <= length; i++) {
            result.add(testDto(i));
        }
        return result;
    }

    private ItemResponseDto testDto(long id) {

        return ItemResponseDto
                .builder()
                .id(id)
                .name("name")
                .description("description")
                .available(true)
                .requestId(10L)
                .nextBooking(ItemResponseDto.BookingDto.builder()
                        .id(4L)
                        .start(LocalDateTime.now().plusDays(1))
                        .end(LocalDateTime.now().plusDays(3))
                        .bookerId(26L)
                        .build())
                .lastBooking(ItemResponseDto.BookingDto.builder()
                        .id(1L)
                        .start(LocalDateTime.now().minusDays(3))
                        .end(LocalDateTime.now().minusDays(1))
                        .bookerId(25L)
                        .build())
                .comments(List.of(ItemResponseDto.CommentDto.builder()
                                .authorName("name_user")
                                .created(LocalDateTime.now().minusDays(2))
                                .text("some text")
                                .build(),
                        ItemResponseDto.CommentDto.builder()
                                .authorName("name_sec_user")
                                .created(LocalDateTime.now().minusDays(2))
                                .text("some text too")
                                .build())
                )
                .build();


    }
}
