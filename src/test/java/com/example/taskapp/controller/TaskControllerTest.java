package com.example.taskapp.controller;

import com.example.taskapp.dto.TaskDto;
import com.example.taskapp.model.Task;
import com.example.taskapp.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
        taskRepository.saveAll(Arrays.asList(
                Task.builder().title("Test Task 1").summary("Test Summary 1").dueDate(LocalDate.now().plusDays(1)).build(),
                Task.builder().title("Test Task 2").summary("Test Summary 2").dueDate(LocalDate.now().plusDays(2)).build()
        ));
    }


    @Test
    public void testCreateTask() throws Exception {
        TaskDto dto = TaskDto.builder()
                .title("Sample Task")
                .summary("This is a sample task")
                .dueDate(LocalDate.now().plusDays(7))
                .build();

        MockHttpServletResponse result = mockMvc.perform(post("/tasks")
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
    public void testUpdateTask() throws Exception {
        Task task = taskRepository.save(Task.builder().title("Sample Task")
                .summary("This is a sample task")
                .dueDate(LocalDate.now().plusDays(7)).build());

        TaskDto dto = TaskDto.builder()
                .id(task.getId())
                .title("New changed title")
                .summary("New changed summary")
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        MockHttpServletResponse result = mockMvc.perform(put("/tasks/" + task.getId())
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
    public void findAllTasks_returnsAllTasks() throws Exception {
    MockHttpServletResponse result =  mockMvc.perform(get("/tasks")
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
}
