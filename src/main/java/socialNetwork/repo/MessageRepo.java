package socialNetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import socialNetwork.db.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
