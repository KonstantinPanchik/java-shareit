package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Data
@Entity
@Table(name = "requests")
@Builder
@NamedEntityGraph(name = "ItemRequest.items", attributeNodes = {@NamedAttributeNode("items")})
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor")
    User requestor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "request")
    List<Item> items;

    LocalDateTime created;

    public ItemRequest() {
    }

    public ItemRequest(Long id, String description, User requestor, List<Item> items, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.items = items;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(requestor, that.requestor) &&
                Objects.equals(items, that.items) &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requestor, items, created);
    }
}
