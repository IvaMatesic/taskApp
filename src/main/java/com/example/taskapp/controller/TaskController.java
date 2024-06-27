package com.example.taskapp.controller;

import com.example.taskapp.dto.TaskDto;
import com.example.taskapp.model.Task;
import com.example.taskapp.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RequestMapping(value = "/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        Task createdTask = taskService.createTask(modelMapper.map(taskDto, Task.class));
        return ResponseEntity.ok(modelMapper.map(createdTask, TaskDto.class));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable(name = "id") Long taskId ,@RequestBody TaskDto taskDto) {
        Task createdTask = taskService.updateTask(taskId, modelMapper.map(taskDto, Task.class));
        return ResponseEntity.ok(modelMapper.map(createdTask, TaskDto.class));
    }

    @GetMapping()
    public ResponseEntity<List<TaskDto>> findAll() {
        List<TaskDto> taskDtos =  taskService.findAll().stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
