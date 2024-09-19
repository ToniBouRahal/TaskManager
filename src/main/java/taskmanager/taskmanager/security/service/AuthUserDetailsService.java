package taskmanager.taskmanager.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taskmanager.taskmanager.model.Role;
import taskmanager.taskmanager.model.User;
import taskmanager.taskmanager.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("Username not found");
        }
        Set<GrantedAuthority> grantedAuthorities = mapRolesToAuthorities(user.get().getRole());
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(), grantedAuthorities);
    }

    public Set<GrantedAuthority> mapRolesToAuthorities(Role role) {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }
}
