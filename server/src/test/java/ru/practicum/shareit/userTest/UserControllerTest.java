package ru.practicum.shareit.userTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService mockUserService;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void saveUser() throws Exception {
        User user = getNewUser("username", "username@mail.ru");

        when(mockUserService.addUser(user))
                .thenAnswer(invocationOnMock -> {
                            User gotUser = invocationOnMock.getArgument(0);
                            gotUser.setId(1L);
                            return gotUser;
                        }
                );


        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void getUserTest() throws Exception {
        when(mockUserService.getUser(anyLong()))
                .thenReturn(getOneUser(1L, "userName", "user@mail.ru"));


        mockMvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("userName")))
                .andExpect(jsonPath("$.email", is("user@mail.ru")));
    }

    @Test
    public void deleteUserTest() throws Exception {


        mockMvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User with id 1 has been deleted")));

    }


    @Test
    public void shouldGetAll() throws Exception {

        when(mockUserService.getAllUsers())
                .thenReturn(createSimpleUsersForTest(5));


        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

    }

    private User getNewUser(String name, String email) {
        return new User(null, name, email);
    }

    private User getOneUser(Long id, String name, String email) {
        return new User(id, name, email);
    }

    private List<User> createSimpleUsersForTest(int amount) {
        List<User> result = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            result.add(getOneUser((long) i, "name" + i, "simplemail" + i + "@mail.ru"));

        }
        return result;
    }
}
