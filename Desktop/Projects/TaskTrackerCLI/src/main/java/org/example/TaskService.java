package org.example;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task add(String description) {
        if(description==null||description.isEmpty()){
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        List<Task> tasks=taskRepository.loadAll();
        long id= taskRepository.nextId(tasks);
        String now= Instant.now().toString();
        Task t=new Task(id,description.trim(),"todo",now,now);
        tasks.add(t);
        taskRepository.saveAll(tasks);
        return t;
    }
    public void update(long id, String description) {
        if(description==null||description.isEmpty()){
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        List<Task> tasks=taskRepository.loadAll();
        Task t=findById(tasks,id);
        t.setDescription(description.trim());
        t.setUpdatedAt(Instant.now().toString());
        taskRepository.saveAll(tasks);
    }
    public void delete(long id) {
        List<Task> tasks=taskRepository.loadAll();
        Task t=findById(tasks,id);
        tasks.remove(t);
        taskRepository.saveAll(tasks);
    }
    public void markStatus(long id, String status) {
        if (!status.equals("todo") && !status.equals("in-progress") && !status.equals("done"))
            throw new IllegalArgumentException("Invalid status: " + status);
        List<Task> tasks=taskRepository.loadAll();
        Task t=findById(tasks,id);
        t.setStatus(status);
        t.setUpdatedAt(Instant.now().toString());
        taskRepository.saveAll(tasks);
    }
    public List<Task> list(String statusFilter){
        List<Task> tasks=taskRepository.loadAll();
        List<Task> result=new ArrayList<Task>();
        for(Task t:tasks){
            if(statusFilter==null||t.getStatus().equalsIgnoreCase(statusFilter)){
                result.add(t);
            }
        }
        return result;
    }
    private Task findById(List<Task> tasks,long id) {
        return tasks.stream().filter(task -> task.getId()==id).findFirst().orElseThrow(()-> new IllegalArgumentException("Task with ID" + id + " not found"));
    }
}
