package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item as i JOIN FETCH i.user as u WHERE u.id=?1 Order by i.id")
    List<Item> userItems(Long userId);

    @Query("select i from Item as i WHERE (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) AND i.available=true ")
    List<Item> search(String text);
}
