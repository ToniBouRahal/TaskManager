package taskmanager.taskmanager.service;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanager.taskmanager.model.Role;
import taskmanager.taskmanager.model.User;
import taskmanager.taskmanager.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {


    //private final BCryptPasswordEncoder passwordEncoder;
    public final UserRepository userRepository ;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username) ;
    }
    public void elevateUserToAdmin(User user) {
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}
