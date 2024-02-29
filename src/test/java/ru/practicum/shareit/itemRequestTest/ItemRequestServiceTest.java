package ru.practicum.shareit.itemRequestTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest

public class ItemRequestServiceTest {

    @MockBean
    ItemRequestRepository itemRequestRepository;

    @MockBean
    UserService userService;

    @Autowired
    ItemRequestService itemRequestService;

    @Test
    public void shouldAddRequest() {
        ItemRequest itemRequest = ItemRequest.builder().description("Описание нового запроса").build();

        when(userService.getUser(anyLong()))
                .thenReturn(getCreator());

        when(itemRequestRepository.save(itemRequest))
                .thenAnswer(invocationOnMock -> {
                    ItemRequest itemRequest2 = invocationOnMock.getArgument(0);
                    itemRequest2.setId(25L);
                    return itemRequest2;
                });

        ItemRequestDto result = itemRequestService.addRequest(itemRequest, 2L);
        assertEquals(result.getDescription(), itemRequest.getDescription());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void shouldGetRequest() {


        when(userService.getUser(anyLong()))
                .thenReturn(getCreator());

        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(getRequests().get(0)));

        ItemRequestDto result = itemRequestService.getRequest(1L, 2L);
        assertEquals(result.getDescription(), getRequests().get(0).getDescription());
        assertEquals(result.getItems().size(), getRequests().get(0).getItems().size());
        assertEquals(result.getCreated(), getRequests().get(0).getCreated());
    }

    @Test
    public void shouldGetItemRequest() {


        when(userService.getUser(anyLong()))
                .thenReturn(getCreator());

        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(getRequests().get(0)));

        ItemRequestDto result = itemRequestService.getRequest(1L, 2L);
        assertEquals(result.getDescription(), getRequests().get(0).getDescription());
        assertEquals(result.getItems().size(), getRequests().get(0).getItems().size());
        assertEquals(result.getCreated(), getRequests().get(0).getCreated());
    }

    @Test
    public void shouldGetItemRequestOfUser() {


        when(userService.getUser(anyLong()))
                .thenReturn(getRequestor());

        when(itemRequestRepository.findItemRequestByRequestorOrderByCreatedDesc(getRequestor()))
                .thenReturn(getRequests());

        List<ItemRequestDto> result = itemRequestService.getRequests(1L);

        assertEquals(result.size(), 3);

    }

    @Test
    public void shouldGetItemRequests() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("created").descending());


        when(userService.getUser(anyLong()))
                .thenReturn(getRequestor());

        when(itemRequestRepository.findAll(pageable))
                .thenReturn(new PageImpl(getRequests()));

        List<ItemRequestDto> result = itemRequestService.getRequestsNotCurrentUser(0, 3, 1L);

        assertEquals(result.size(), 3);
        assertEquals(getRequests().get(0).getDescription(), result.get(0).getDescription());

    }

    private List<ItemRequest> getRequests() {

        ItemRequest three = ItemRequest.builder()
                .id(1L)
                .requestor(getRequestor())
                .created(LocalDateTime.of(2001, 12, 01, 12, 00))
                .description("Ножницы для маникюра")
                .items(getItems(0, 4))
                .build();

        ItemRequest two = ItemRequest.builder()
                .id(2L)
                .requestor(getRequestor())
                .created(LocalDateTime.of(2002, 12, 01, 12, 00))
                .description("Ножницы для маникюра")
                .items(getItems(5, 2))
                .build();

        ItemRequest one = ItemRequest.builder()
                .id(3L)
                .requestor(getRequestor())
                .created(LocalDateTime.of(2003, 12, 01, 12, 00))
                .description("Ножницы для маникюра")
                .items(getItems(10, 3))
                .build();

        setRequest(one.getItems(), one);
        setRequest(two.getItems(), two);
        setRequest(three.getItems(), three);

        return List.of(one, two, three);

    }

    private void setRequest(List<Item> items, ItemRequest itemRequest) {
        for (Item item : items) {
            item.setRequest(itemRequest);
        }
    }

    private List<Item> getItems(int idStart, int length) {
        List<Item> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {


            Item item = new Item();
            item.setId((long) ++idStart);
            item.setName("Имя предмета " + idStart);
            item.setDescription("Описание предмета " + idStart);
            item.setAvailable(true);
            item.setUser(getCreator());
            item.setComments(new ArrayList<>());

            result.add(item);
        }
        return result;
    }

    private User getRequestor() {
        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("Admin");
        requestor.setEmail("Admin@mail.ru");
        return requestor;
    }

    private User getCreator() {
        User creator = new User();
        creator.setId(2L);
        creator.setName("Kostya");
        creator.setEmail("my@mail.ru");
        return creator;
    }


}
