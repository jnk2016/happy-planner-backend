package com.jnk2016.happyplannerbackend.task;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private ApplicationUserRepository applicationUserRepository;
    private TaskRepository taskRepository;
    private Sort sort = Sort.by("priority").ascending();

    public TaskController(ApplicationUserRepository applicationUserRepository, TaskRepository taskRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.taskRepository = taskRepository;
    }

    /** Create a new task for the logged in user */
    @PostMapping
    public void newTask(Authentication auth, @RequestBody Task task){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        task.setUser(user);
        taskRepository.save(task);
    }

    /** Get all tasks by each category and status */
    @GetMapping
    public HashMap<String, Object> getTasksSortedByCategory(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        HashMap<String, Object> response = new HashMap<>();
        List<Task> taskData = taskRepository.findByUser(user, sort);
        for(Task entry : taskData){
            String priorityKey = (entry.getPriority() == 0) ? "upcoming" : (entry.getPriority() == 1) ? "high" :
                                (entry.getPriority() == 2) ? "medium" : "low";
            String completionStatus = entry.getStatus() ? "completed" : "todo";

            HashMap<String, Object> tasksOfPriority = new HashMap<>();
            List<HashMap<String, Object>> tasksOfStatus = new ArrayList<>();
            if(response.containsKey(priorityKey)){
                tasksOfPriority = (HashMap<String, Object>)response.get(priorityKey);
                if(tasksOfPriority.containsKey(completionStatus)){
                    tasksOfStatus = (List<HashMap<String, Object>>) tasksOfPriority.get(completionStatus);
                }
            }
            tasksOfStatus.add(entry.toResponse());
            tasksOfPriority.put(completionStatus, tasksOfStatus);
            response.put(priorityKey, tasksOfPriority);
        }
        return response;
    }

    /** Update a task using id */
    @PutMapping("/{id}")
    public HashMap<String, Object> updateTask(@PathVariable long id, @RequestBody Task requestBody) throws Exception{
        Task task = taskRepository.findById(id).orElseThrow(()-> new Exception("Task not found!"));
        task.setName(requestBody.getName());
        task.setStatus(requestBody.getStatus());
        taskRepository.save(task);

        return task.toResponse();
    }

}
