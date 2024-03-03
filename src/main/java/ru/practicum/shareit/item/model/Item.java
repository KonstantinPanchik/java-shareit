package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Builder
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Size(min = 3, max = 200)
    String name;
    @Size(max = 500)
    String description;
    @NotNull
    Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    ItemRequest request;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    List<Comment> comments;


    public Item() {
    }

    public Item(Long id, String name, String description, Boolean available, User user, ItemRequest request, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.user = user;
        this.request = request;
        this.comments = comments;
    }
}
