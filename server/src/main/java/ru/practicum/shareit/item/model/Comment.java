package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    String text;
    @Column(name = "item_id")
    Long itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author")
    User author;

    LocalDateTime created;


}
