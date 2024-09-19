package taskmanager.taskmanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taskmanager.taskmanager.dto.JwtAuthResponse;
import taskmanager.taskmanager.dto.LoginRequest;
import taskmanager.taskmanager.dto.RegisterRequest;
import taskmanager.taskmanager.exceptions.UsernameExistsException;
import taskmanager.taskmanager.model.Role;
import taskmanager.taskmanager.model.User;
import taskmanager.taskmanager.repository.UserRepository;
import taskmanager.taskmanager.service.AuthService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegisterRequest registerRequest) {
        String response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login (@RequestBody LoginRequest loginRequest) {
        String response = authService.login(loginRequest);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(response);
        return ResponseEntity.ok(jwtAuthResponse);
    }

}
