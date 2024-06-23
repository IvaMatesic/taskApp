package com.example.taskapp.unit;

import com.example.taskapp.model.Task;
import com.example.taskapp.repository.TaskRepository;
import com.example.taskapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;


    @Test
    public void testCreateTask() {
        Task task = Task.builder().title("Test Task").summary("This is a test task").build();
        Task savedTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .summary("This is a test task")
                .dueDate(LocalDate.now().plusDays(7))
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        Task result = taskService.createTask(task);

        assertEquals(savedTask.getId(), result.getId());
        assertEquals(savedTask.getTitle(), result.getTitle());
        assertEquals(savedTask.getSummary(), result.getSummary());
        assertEquals(savedTask.getDateOfCreation(), result.getDateOfCreation());
        assertEquals(savedTask.getDueDate(), result.getDueDate());
    }


}
