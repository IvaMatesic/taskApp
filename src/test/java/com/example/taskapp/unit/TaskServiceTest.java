package com.example.taskapp.unit;

import com.example.taskapp.model.Task;
import com.example.taskapp.repository.TaskRepository;
import com.example.taskapp.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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


    @Test
    public void testUpdateTask_TaskExists_UpdatedSuccessfully() {
        Long taskId = 1L;

        Task existingTask = Task.builder()
                .id(taskId)
                .title("Old Title")
                .summary("Old Summary")
                .dueDate(LocalDate.now().plusDays(2))
                .build();

        Task updatedTask = Task.builder()
                .id(taskId)
                .title("New Title")
                .summary("New Summary")
                .dueDate(LocalDate.now().plusDays(3))
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(taskId, updatedTask);

        Assertions.assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Summary", result.getSummary());
        assertEquals(LocalDate.now().plusDays(3), result.getDueDate());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    public void testUpdateTask_TaskNotFound() {
        Long taskId = 1L;
        Task updatedTask = Task.builder()
                .title("New Title")
                .summary("New Summary")
                .dueDate(LocalDate.now().plusDays(1))
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(taskId, updatedTask));

        assertEquals("Task with id " + taskId + " not found", thrown.getMessage());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }


    @Test
    public void findAll_returnsAllTasks() {
        List<Task> tasks = Arrays.asList(
                Task.builder().title("Test Task 1").summary("Test Summary 1").dueDate(LocalDate.now().plusDays(1)).build(),
                Task.builder().title("Test Task 2").summary("Test Summary 2").dueDate(LocalDate.now().plusDays(2)).build()
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.findAll();

        assertEquals(2, result.size());
        assertEquals("Test Task 1", result.get(0).getTitle());
        assertEquals("Test Summary 1", result.get(0).getSummary());
        assertEquals("Test Task 2", result.get(1).getTitle());
        assertEquals("Test Summary 2", result.get(1).getSummary());
    }

    @Test
    public void deleteTask_taskExists_deletedSuccessfully() {
        Long taskId = 1L;
        Task taskToDelete = Task.builder().title("Test Task 1").summary("Test Summary 1").dueDate(LocalDate.now().plusDays(1)).build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(taskToDelete));

        taskService.deleteById(taskId);

        verify(taskRepository).delete(taskToDelete);
    }

    @Test
    public void deleteTask_taskDoesntExist_throwsEntityNotFoundException() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> taskService.deleteById(taskId));

        assertEquals("Task with id " + taskId + " not found", thrown.getMessage());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).delete(any(Task.class));
    }


}
