package taskmanager.taskmanager.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import taskmanager.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    public Optional<User> findByUsername(String username);
    public Boolean existsByUsername(String username);
}
