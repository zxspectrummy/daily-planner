package com.example.dailyplanner.controller;

import com.example.dailyplanner.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.dailyplanner.TestUtils.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskControllerTest {
    private static final int TASK_COUNT = 3;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTask() throws Exception {
        createRandomTasks(1);
    }

    @Test
    public void getTasks() throws Exception {
        List<Task> tasks = createRandomTasks(TASK_COUNT);
        List<JSONObject> jsonTasks = tasks.stream().map(t -> {
            try {
                return (JSONObject) JSONValue.parse(objectMapper.writeValueAsString(t));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        mvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(TASK_COUNT)))
                .andExpect(jsonPath("$").value(jsonTasks));
    }

    @Test
    public void deleteTask() throws Exception {
        List<Task> tasks = createRandomTasks(TASK_COUNT);
        String taskEndpoint = String.format("/tasks/%d", tasks.get(0).getId());
        mvc.perform(MockMvcRequestBuilders.delete(taskEndpoint))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(TASK_COUNT - 1)));
        mvc.perform(MockMvcRequestBuilders.get(taskEndpoint))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTask() throws Exception {
        List<Task> tasks = createRandomTasks(TASK_COUNT);
        String taskEndpoint = String.format("/tasks/%d", tasks.get(0).getId());
        Task updatedTask = tasks.get(0);
        updatedTask.setDescription("updated description");
        mvc.perform(MockMvcRequestBuilders.put(taskEndpoint).content(objectMapper.writeValueAsString(tasks.get(0)))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get(taskEndpoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(updatedTask))));
    }

    @Test
    public void markAsDone() throws Exception {
        Task task = createRandomTasks(1).get(0);
        task.setDone(true);
        String taskEndpoint = String.format("/tasks/%d", task.getId());
        mvc.perform(MockMvcRequestBuilders.patch(taskEndpoint))
                .andExpect(status().isOk())
                .andDo(result -> mvc.perform(MockMvcRequestBuilders.get(taskEndpoint))
                        .andExpect(content().string(equalTo(objectMapper.writeValueAsString(task)))));
    }

    private List<Task> createRandomTasks(int taskCount) throws Exception {
        List<Task> createdTasks = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            LocalDate randomDate = getRandomLocalDate();
            final Task task = new Task(randomDate, getRandomString(10));
            final String expectedResponseContent = objectMapper.writeValueAsString(task);
            mvc.perform(MockMvcRequestBuilders.post("/tasks").content(expectedResponseContent)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.description").value(task.getDescription()))
                    .andExpect(jsonPath("$.date").value(task.getDate().toString()))
                    .andDo(createResult -> {
                        String json = createResult.getResponse().getContentAsString();
                        Task createdTask = (Task) convertJSONStringToObject(json, Task.class);
                        mvc.perform(MockMvcRequestBuilders.get(String.format("/tasks/%d", createdTask.getId()))
                                        .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(createdTask))));
                        createdTasks.add(createdTask);
                    });
        }
        return createdTasks;
    }
}
