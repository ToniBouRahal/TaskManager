package taskmanager.taskmanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taskmanager.taskmanager.dto.TaskDTO;
import taskmanager.taskmanager.exceptions.InvalidAttributeException;
import taskmanager.taskmanager.exceptions.InvalidSortException;
import taskmanager.taskmanager.exceptions.TaskNotFoundException;
import taskmanager.taskmanager.exceptions.UserNotFoundException;
import taskmanager.taskmanager.model.Task;
import taskmanager.taskmanager.model.User;
import taskmanager.taskmanager.service.TaskService;
import taskmanager.taskmanager.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequestMapping("/api/tasks")
@RestController
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    public final UserService userService ;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/task")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return getListResponseEntity(tasks);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/task/id/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable String id) {
        Optional<Task> task = taskService.getTaskByDisplayId(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
        TaskDTO dto = new TaskDTO();
        dto.setDisplayId(task.get().getDisplayId());
        dto.setTitle(task.get().getTitle());
        dto.setDescription(task.get().getDescription());
        dto.setPriority(task.get().getPriority());
        dto.setStatus(task.get().getStatus());
        dto.setCreationDate(task.get().getCreationDate());
        dto.setDeadline(task.get().getDeadline());
        dto.setUsername(task.get().getUser().getUsername());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/task/username/{username}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        Optional<List<Task>> tasks = Optional.of(taskService.getTaskByUser(user.orElse(null)).orElse(new ArrayList<>()));
        return new ResponseEntity<>(tasks.get(), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/task")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Optional<User> user = userService.findByUsername(task.getUser().getUsername());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        task.setUser(user.get());
        Task savedTask = taskService.addTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/task/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task task) {
        Optional<Task> taskOptional = taskService.getTaskByDisplayId(id);
        if (taskOptional.isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
        Task updatedTask = taskService.updateTask(taskOptional.get().getDisplayId(), task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/task/{id}/in-progress")
    public ResponseEntity<Task> markTaskInProgress(@PathVariable String id) {
        Optional<Task> taskOptional = taskService.getTaskByDisplayId(id);
        if (taskOptional.isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
        Task updatedTask = taskService.markTaskInProgress(taskOptional.get());
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/task/{id}/completed")
    public ResponseEntity<Task> markTaskCompleted(@PathVariable String id) {
        Optional<Task> taskOptional = taskService.getTaskByDisplayId(id);
        if (taskOptional.isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
        Task updatedTask = taskService.markTaskCompleted(taskOptional.get());
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
    @GetMapping("/task/sorted")
    public ResponseEntity<List<TaskDTO>> getAllTasksSorted(@RequestParam String attribute, @RequestParam String direction) {
        if (!taskService.isValidAttribute(attribute)) {
            throw new InvalidAttributeException("Invalid attribute: " + attribute);
        }
        if (!taskService.isValidDirection(direction)) {
            throw new InvalidSortException("Invalid direction: " + direction);
        }
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        List<Task> tasks = taskService.getAllTasksSorted(attribute, sortDirection);
        return getListResponseEntity(tasks);
    }

    private ResponseEntity<List<TaskDTO>> getListResponseEntity(List<Task> tasks) {
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setDisplayId(task.getDisplayId());
                    dto.setTitle(task.getTitle());
                    dto.setDescription(task.getDescription());
                    dto.setPriority(task.getPriority());
                    dto.setStatus(task.getStatus());
                    dto.setCreationDate(task.getCreationDate());
                    dto.setDeadline(task.getDeadline());
                    dto.setUsername(task.getUser().getUsername());
                    return dto;
                })
                .toList();
        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/task/search")
    public ResponseEntity<List<TaskDTO>> search(@RequestParam String searchTerm) {
        Optional<List<Task>> tasks = taskService.search(searchTerm);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found");
        }

        return getListResponseEntity(tasks.get());
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
    @GetMapping("/task/paged")
    public ResponseEntity<Page<TaskDTO>> getAllTasksPaged(@RequestParam int page, @RequestParam int size) {
        Page<Task> tasks = taskService.getAllTasksPaged(page, size);
        Page<TaskDTO> taskDTOs = tasks.map(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setDisplayId(task.getDisplayId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setPriority(task.getPriority());
            dto.setStatus(task.getStatus());
            dto.setCreationDate(task.getCreationDate());
            dto.setDeadline(task.getDeadline());
            dto.setUsername(task.getUser().getUsername());
            return dto;
        });
        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }
}
