package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @NotBlank
    String text;
    @Column(name = "item_id")
    Long itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author")
    User author;

    LocalDateTime created;


}
