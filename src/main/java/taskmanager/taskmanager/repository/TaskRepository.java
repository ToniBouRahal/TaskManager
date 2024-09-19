package taskmanager.taskmanager.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import taskmanager.taskmanager.model.Task;
import taskmanager.taskmanager.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends MongoRepository<Task, ObjectId> {
public List<Task> findAll();
public Optional<Task> findByDisplayId(String displayId);
public Optional<Task> findByTitle(String title);
public Optional<List<Task>> findByPriority(int priority);
public Optional<List<Task>> findByStatus(String status);
public Optional<List<Task>> findByCreationDate(Date creationDate);
public Optional<List<Task>> findByDeadline(Date deadline);
public Optional<List<Task>> findByUser(User user);
public List<Task> findAll(Sort sort);
@Query("{'$or' : [{'title': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}]}")
public Optional<List<Task>> search(String searchTerm);
Page<Task> findAll(Pageable pageable);

}
