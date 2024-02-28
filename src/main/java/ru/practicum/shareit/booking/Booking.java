package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Builder
@Entity
@Table(name = "bookings")
@NamedEntityGraph(name = "Bookings.bookerAndItem", attributeNodes = {@NamedAttributeNode("booker"),
        @NamedAttributeNode("item")})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "started")
    LocalDateTime start;
    @Column(name = "ended")
    LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker")
    User booker;
    @Enumerated(value = EnumType.STRING)
    Status status;

    public Booking(Long id, LocalDateTime start, LocalDateTime end, Item item, User booker, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }

    public Booking() {
    }
}
