package com.example.project.controller;

import com.example.project.UserRegistrationController;
import com.example.project.model.UserRegistration;
import com.example.project.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserRegistrationController.class)
public class UserRegistrationControllerTest {

    private MockMvc mockMvc;

    @MockBean
    UserRepository repo;

    @BeforeEach
    public void setUp() {
        UserRegistrationController controller = new UserRegistrationController(repo);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void testGetUserById() throws Exception {
        // arrange
        int userId = 1;
        String json = """
                {
                    "id": 1,
                    "firstName": "John",
                    "lastName": "Doe",
                    "street": "123 Main Street",
                    "city": "City",
                    "state": "State",
                    "zip": "ZIP",
                    "country": "Country",
                    "phone": "555-123-4567",
                    "email": "johndoe@example.com",
                    "username": "johndoe123"
                }""";

        Mockito.when(repo.findById(1L)).thenReturn(new UserRegistration(1L, "John", "Doe", "123 Main Street", "City", "State", "ZIP", "Country", "555-123-4567", "johndoe@example.com", "johndoe123"));

        // act and assert
        mockMvc.perform(MockMvcRequestBuilders.get("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }
    @Test
    public void testGetUserByIdFail() throws Exception {
        int userId = 1;
        String json = "{\n\"error\":\"User not found.\"\n}";

        Mockito.when(repo.findById(1L)).thenReturn(null);

        // act and assert
        mockMvc.perform(MockMvcRequestBuilders.get("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    public void testCreateUser() throws Exception {
        // arrange
        String json = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"street\":\"212 Beyer Ct\",\"city\":\"Ames\",\"state\":\"Iowa\",\"zip\":\"50012\",\"country\":\"US\",\"phone\":\"515-666-7777\",\"email\":\"john.doe@example.com\",\"username\":\"compleatlkajdsaklsdfja\"}";


        // act and assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateUserFail() throws Exception {
        // arrange
        String json = "{\"firstName\":\"\",\"lastName\":\"Doe\",\"street\":\"212 Beyer Ct\",\"city\":\"City\",\"state\":\"Iowa\",\"zip\":\"50012\",\"country\":\"US\",\"phone\":\"515-666-7777\",\"email\":\"john.doe@example.com\",\"username\":\"asdfa\"}";


        // act and assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



}