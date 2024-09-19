package taskmanager.taskmanager.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import taskmanager.taskmanager.exceptions.TaskNotFoundException;
import taskmanager.taskmanager.model.Task;
import taskmanager.taskmanager.model.User;
import taskmanager.taskmanager.repository.TaskRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    public Optional<List<Task>> getTaskByUser(User user) {
        return taskRepository.findByUser(user);
    }
    public Optional<Task> getTaskByDisplayId(String displayId) {
        return taskRepository.findByDisplayId(displayId);
    }
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(String id) {
        Optional<Task> task = taskRepository.findByDisplayId(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
        taskRepository.delete(task.get());
    }

    public Task updateTask(String id, Task task) {
        Optional<Task> taskOptional = taskRepository.findByDisplayId(id);
        if (taskOptional.isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
        Task taskToUpdate = taskOptional.get();
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setPriority(task.getPriority());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setDeadline(task.getDeadline());
        return taskRepository.save(taskToUpdate);
    }

    public List<Task> getAllTasksSorted(String attribute , Sort.Direction direction){
        return taskRepository.findAll(Sort.by(direction, attribute));
    }

    public boolean isValidAttribute(String attribute) {
        Field[] fields = Task.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(attribute)) {
                return true;
            }
        }
        return false;
    }
    public boolean isValidDirection(String direction) {
        return direction.equalsIgnoreCase("ASC") || direction.equalsIgnoreCase("DESC");
    }

    public Task markTaskInProgress(Task task) {
        task.setStatus("In Progress");
        return taskRepository.save(task);
    }
    public Task markTaskCompleted(Task task) {
        task.setStatus("Completed");
        return taskRepository.save(task);
    }
    public Optional<List<Task>> search(String searchTerm) {
        return taskRepository.search(searchTerm);
    }

    public Page<Task> getAllTasksPaged(int page, int size) {
        return taskRepository.findAll(PageRequest.of(page, size));
    }

}

