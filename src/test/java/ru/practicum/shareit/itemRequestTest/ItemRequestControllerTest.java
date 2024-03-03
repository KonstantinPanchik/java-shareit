package ru.practicum.shareit.itemRequestTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;

    ItemRequest itemRequest = ItemRequest.builder().description("Нужна газонокосилка").build();

    ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("Нужна газонокосилка")
            .id(1L)
            .created(LocalDateTime.of(2001, 9, 11, 10, 55, 48))
            .items(new ArrayList<>())
            .build();

    @Test
    public void saveItemRequest() throws Exception {
        when(itemRequestService.addRequest(itemRequest, 1L))
                .thenReturn(itemRequestDto);


        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().toString())))
                .andExpect(jsonPath("$.items", is(itemRequestDto.getItems())));
    }

    @Test
    public void getItemRequest() throws Exception {
        when(itemRequestService.getRequest(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);


        mockMvc.perform(get("/requests/1")

                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().toString())))
                .andExpect(jsonPath("$.items", is(itemRequestDto.getItems())));
    }

    @Test
    public void getAllItemRequest() throws Exception {

        when(itemRequestService.getRequests(1L))
                .thenReturn(getRequests());


        mockMvc.perform(get("/requests")

                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

    }

    @Test
    public void getAllRequests() throws Exception {

        when(itemRequestService.getRequestsNotCurrentUser(anyInt(), anyInt(), anyLong()))
                .thenReturn(getRequests());


        mockMvc.perform(get("/requests/all?from=0&size=3")

                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

    }


    private List<ItemRequestDto> getRequests() {

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("Нужна газонокосилка")
                .id(1L)
                .created(LocalDateTime.of(2001, 9, 11, 10, 55, 48))
                .items(new ArrayList<>())
                .build();

        ItemRequestDto itemRequestDto2 = ItemRequestDto.builder()
                .description("Нужно остановить конец света и рулетка")
                .id(2L)
                .created(LocalDateTime.of(2012, 12, 12, 12, 12, 12))
                .items(new ArrayList<>())
                .build();

        ItemRequestDto itemRequestDto3 = ItemRequestDto.builder()
                .description("Нужна собачья упряжка ")
                .id(3L)
                .created(LocalDateTime.of(2008, 7, 1, 23, 47, 45))
                .items(new ArrayList<>())
                .build();

        return List.of(itemRequestDto, itemRequestDto2, itemRequestDto3);
    }
}
