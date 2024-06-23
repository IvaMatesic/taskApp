package com.example.taskapp.controller;

import com.example.taskapp.dto.TaskDto;
import com.example.taskapp.model.Task;
import com.example.taskapp.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
}
