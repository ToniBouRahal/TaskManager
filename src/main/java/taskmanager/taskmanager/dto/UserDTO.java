package taskmanager.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskmanager.taskmanager.model.Role;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private Role role;
}
