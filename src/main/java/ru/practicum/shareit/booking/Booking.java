package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;

        boolean isItemEquals;
        if (item == null && booking.item == null) {
            isItemEquals = true;
        } else {
            if (item == null || booking.item == null) {
                isItemEquals = false;
            } else {
                isItemEquals = item.getId() == booking.item.getId();
            }
        }

        boolean isBookerEquals;
        if (booker == null && booking.booker == null) {
            isBookerEquals = true;
        } else {
            if (booker == null || booking.booker == null) {
                isBookerEquals = false;
            } else {
                isBookerEquals = booker.getId() == booking.booker.getId();
            }
        }

        return Objects.equals(id, booking.id)
                && Objects.equals(start, booking.start)
                && Objects.equals(end, booking.end)
                && isItemEquals
                && isBookerEquals
                && status == booking.status;
    }

    @Override
    public int hashCode() {
        long itemId = 0L;
        long bookerId = 0L;

        if (item != null) {
            itemId = item.getId();
        }
        if (booker != null) {
            bookerId = booker.getId();
        }


        return Objects.hash(id, start, end, itemId, bookerId, status);
    }
}
