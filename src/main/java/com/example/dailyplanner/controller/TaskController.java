package com.example.dailyplanner.controller;

import com.example.dailyplanner.model.Task;
import com.example.dailyplanner.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/tasks")
    public Task create(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable long id) throws ResourceNotFoundException {
        return taskRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/tasks")
    public Iterable<Task> getTasks() {
        return taskRepository.findAll();
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable long id) {
        taskRepository.deleteById(id);
    }

    @PatchMapping("/tasks/{id}")
    public void markAsDone(@PathVariable long id) {
        taskRepository.markAsDone(id);
    }
}
