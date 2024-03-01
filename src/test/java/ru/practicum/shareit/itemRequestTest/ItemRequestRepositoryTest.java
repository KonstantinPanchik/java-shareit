package ru.practicum.shareit.itemRequestTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest

public class ItemRequestRepositoryTest {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public ItemRequestRepositoryTest(ItemRequestRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Test
    public void add() {
        User vitya = new User(null, "вВитёк", "vitek@gmail.com");
        assertNull(vitya.getId());
        userRepository.save(vitya);
        assertNotNull(vitya.getId());


        ItemRequest itemRequest = ItemRequest.builder()
                .description("Неважно что это за предмет!!!!")
                .requestor(vitya)
                .created(LocalDateTime.now())
                .build();
        assertNull(itemRequest.getId());
        repository.save(itemRequest);
        assertNotNull(itemRequest.getId());
    }

    @Test
    public void getAllRequests() {
        User vitya = new User(null, "вВитёк", "vitek@gmail.com");
        assertNull(vitya.getId());
        userRepository.save(vitya);
        assertNotNull(vitya.getId());

        List<ItemRequest> requests = getTestRequests(3, vitya);

        for (ItemRequest request : requests) {
            repository.save(request);
        }

        List<ItemRequest> fromDb = repository.findItemRequestByRequestorOrderByCreatedDesc(vitya);

        assertEquals(3, fromDb.size());
        assertEquals(requests.get(0), fromDb.get(2));
        assertEquals(requests.get(1), fromDb.get(1));

    }

    @Test
    public void getNotUserRequests() {

        User vitya = new User(null, "Витёк", "vitek@gmail.com");
        assertNull(vitya.getId());
        userRepository.save(vitya);
        assertNotNull(vitya.getId());

        User petr = new User(null, "Петя", "petrPervii@ya.com");
        assertNull(petr.getId());
        userRepository.save(petr);
        assertNotNull(petr.getId());


        List<ItemRequest> requests = getTestRequests(3, vitya);

        for (ItemRequest request : requests) {
            repository.save(request);
        }

        List<ItemRequest> fromDb = repository
                .findItemRequestByRequestorNotOrderByCreatedDesc(petr,
                        PageRequest.of(0, 3, Sort.by("created").descending()))
                .getContent();

        assertEquals(3, fromDb.size());
        assertEquals(requests.get(0), fromDb.get(2));
        assertEquals(requests.get(1), fromDb.get(1));

    }


    private List<ItemRequest> getTestRequests(int length, User requestor) {
        List<ItemRequest> result = new ArrayList<>();

        for (int i = 1; i <= length; i++) {
            ItemRequest request = ItemRequest.builder()
                    .description("Описание предмета " + i)
                    .requestor(requestor)
                    .created(LocalDateTime.now().plusDays(i))
                    .build();
            result.add(request);
        }
        return result;

    }

}
