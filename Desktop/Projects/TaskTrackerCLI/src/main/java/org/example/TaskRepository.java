package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final Path filePath;
    public TaskRepository() {
        this.filePath=Path.of(System.getProperty("user.dir")).resolve("tasks.json");
    }
    public synchronized List<Task> loadAll(){
        File f=filePath.toFile();
        if(!f.exists()){
            saveAll(new ArrayList<>());
            return new ArrayList<>();
        }
        try{
            return objectMapper.readValue(f, new TypeReference<List<Task>>(){});
        } catch (IOException e){
            throw new RuntimeException("Failed to read tasks.json"+e.getMessage());
        }
    }
    public synchronized void saveAll(List<Task> tasks){
        try{
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(),tasks);
        }
        catch (IOException e){
            throw new RuntimeException("Failed to write tasks.json"+e.getMessage());
        }
    }
    public long nextId(List<Task> tasks){
        long max=0L;
        for(Task t:tasks){
            if(t.getId()>max) {
                max = t.getId();
            }
        }
        return max+1;
    }

}
