package taskmanager.taskmanager.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String displayId;
    private String title;
    private String description;
    private int priority;
    private String status;
    private Date creationDate;
    private Date deadline ;
    @DBRef
    private User user;

}

