package taskmanager.taskmanager.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import taskmanager.taskmanager.dto.UserDTO;
import taskmanager.taskmanager.exceptions.UserNotFoundException;
import taskmanager.taskmanager.service.UserService;
import taskmanager.taskmanager.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/users")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}/elevate")
    public ResponseEntity<User> elevateUserToAdmin(@PathVariable String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        userService.elevateUserToAdmin(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setUsername(user.getUsername());
                    dto.setRole(user.getRole());
                    return dto;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

}
