package com.example.dailyplanner.controller;

import com.example.dailyplanner.model.Task;
import com.example.dailyplanner.repository.TaskRepository;
import com.example.dailyplanner.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping
    public Task create(@RequestBody Task task, @AuthenticationPrincipal AuthenticatedUser user) {
        task.setUser(user.getUser());
        return taskRepository.save(task);
    }

    @GetMapping("{id}")
    public Task getTask(@PathVariable long id, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) throws ResourceNotFoundException {
        return taskRepository.findByIdAndUserId(id, authenticatedUser.getId()).orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping
    public Iterable<Task> getTasks(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return taskRepository.findByUserId(authenticatedUser.getId());
    }

    @PutMapping("{id}")
    public Task updateTask(@RequestBody Task task, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) throws NotAuthorizedException {
        long taskOwnerId = taskRepository.findById(task.getId()).get().getId();
        if (authenticatedUser.getUser().getId().equals(taskOwnerId)) {
            task.setUser(authenticatedUser.getUser());
            return taskRepository.save(task);
        }
        throw new NotAuthorizedException();
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable long id) {
        taskRepository.deleteById(id);
    }

    @PatchMapping("{id}")
    public void markAsDone(@PathVariable long id) {
        taskRepository.markAsDone(id);
    }
}
