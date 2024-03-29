package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(ItemCreationDto dto, long userId) {
        return post("", userId, null, dto);
    }

    public ResponseEntity<Object> updateItem(ItemCreationDto itemCreationDto, long userId, long itemId) {
        return patch("/" + itemId, userId, itemCreationDto);
    }

    public ResponseEntity<Object> getItem(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getUserItems(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> search(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text
        );

        return get("/search?from={from}&size={size}&text={text}", null, parameters);
    }

    public ResponseEntity<Object> addComment(CommentCreationDto comment, Long userId, Long itemId) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}
