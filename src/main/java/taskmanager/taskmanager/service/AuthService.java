package taskmanager.taskmanager.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanager.taskmanager.dto.LoginRequest;
import taskmanager.taskmanager.dto.RegisterRequest;
import taskmanager.taskmanager.exceptions.UsernameExistsException;
import taskmanager.taskmanager.model.Role;
import taskmanager.taskmanager.model.User;
import taskmanager.taskmanager.repository.UserRepository;
import taskmanager.taskmanager.security.JwtProvider;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder ;
    private final JwtProvider jwtProvider;

    public String register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new UsernameExistsException("Username already exists");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return "User registered successfully";
    }
    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }
}
