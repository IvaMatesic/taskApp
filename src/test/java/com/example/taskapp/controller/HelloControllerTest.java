package com.example.taskapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setupBeforeEach() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testSayHelloPrivate_unauthenticatedUser_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/private/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSayHelloPublic_unauthenticatedUser_returnsOk() throws Exception {
        mockMvc.perform(get("/public/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello public!"));
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void testSayHelloPrivate_authenticatedUser_returnsOk() throws Exception {
        mockMvc.perform(get("/private/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello private!"));
    }
}
