package com.example.taskapp.controller;

import com.example.taskapp.dto.TaskDto;
import com.example.taskapp.model.Task;
import com.example.taskapp.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TaskControllerTest {
    
    private static final String TASKS_URL = "/private/tasks";

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setupBeforeEach() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        taskRepository.deleteAll();
        taskRepository.saveAll(Arrays.asList(
                Task.builder().title("Test Task 1").summary("Test Summary 1").dueDate(LocalDate.now().plusDays(1)).build(),
                Task.builder().title("Test Task 2").summary("Test Summary 2").dueDate(LocalDate.now().plusDays(2)).build()
        ));
    }


    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void createTask_userIsAdmin_successfullyCreatesTask() throws Exception {
        TaskDto dto = TaskDto.builder()
                .title("Sample Task")
                .summary("This is a sample task")
                .dueDate(LocalDate.now().plusDays(7))
                .build();

        MockHttpServletResponse result = mockMvc.perform(post(TASKS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        TaskDto responseDto = objectMapper.readValue(result.getContentAsString(), TaskDto.class);

        assertThat(responseDto).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(dto);
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void updateTask_userIsAdmin_successfullyUpdatesTask() throws Exception {
        Task task = taskRepository.save(Task.builder().title("Sample Task")
                .summary("This is a sample task")
                .dueDate(LocalDate.now().plusDays(7)).build());

        TaskDto dto = TaskDto.builder()
                .id(task.getId())
                .title("New changed title")
                .summary("New changed summary")
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        MockHttpServletResponse result = mockMvc.perform(put(TASKS_URL + "/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        TaskDto responseDto = objectMapper.readValue(result.getContentAsString(), TaskDto.class);

        assertThat(responseDto).usingRecursiveComparison()
                .isEqualTo(dto);
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void findAllTasks_userIsAuthenticated_returnsAllTasks() throws Exception {
        MockHttpServletResponse result = mockMvc.perform(get(TASKS_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String jsonResponse = result.getContentAsString();

        List<TaskDto> responseDtos = Arrays.asList(objectMapper.readValue(jsonResponse, TaskDto[].class));

        assertThat(responseDtos).hasSize(2);

        TaskDto taskDto1 = responseDtos.get(0);
        assertThat(taskDto1.getTitle()).isEqualTo("Test Task 1");
        assertThat(taskDto1.getSummary()).isEqualTo("Test Summary 1");

        TaskDto taskDto2 = responseDtos.get(1);
        assertThat(taskDto2.getTitle()).isEqualTo("Test Task 2");
        assertThat(taskDto2.getSummary()).isEqualTo("Test Summary 2");
    }

    @Test
    public void findAllTasks_unauthenticatedUser_returnsUnauthorized() throws Exception {
        mockMvc.perform(get(TASKS_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse();
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void deleteTask_taskExists_successfullyDeletesTask() throws Exception {
        Task taskToDelete = taskRepository.findAll().getFirst();

        mockMvc.perform(delete(TASKS_URL + "/" + taskToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


        Optional<Task> deletedTask = taskRepository.findById(taskToDelete.getId());
        assertFalse(deletedTask.isPresent());
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void deleteTask_taskDoesntExists_returnsNotFound() throws Exception {
        mockMvc.perform(delete(TASKS_URL + "/" + 0L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
