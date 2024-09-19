package taskmanager.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private String displayId;
    private String title;
    private String description;
    private int priority;
    private String status;
    private Date creationDate;
    private Date deadline;
    private String username;
}
