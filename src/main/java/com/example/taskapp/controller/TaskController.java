package com.example.taskapp.controller;

import com.example.taskapp.dto.TaskDto;
import com.example.taskapp.model.Task;
import com.example.taskapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
