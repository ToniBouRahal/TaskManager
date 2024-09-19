package taskmanager.taskmanager.security;

public class PasswordEncoder {
    public static void main(String[] args) {
        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin"));
    }
}