package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Override
    @EntityGraph("ItemRequest.items")
    Optional<ItemRequest> findById(Long aLong);


    @EntityGraph("ItemRequest.items")
    Page<ItemRequest> findItemRequestByRequestorNotOrderByCreatedDesc(User requestor, Pageable pageable);

    @EntityGraph("ItemRequest.items")
    List<ItemRequest> findItemRequestByRequestorOrderByCreatedDesc(User requestor);
}
