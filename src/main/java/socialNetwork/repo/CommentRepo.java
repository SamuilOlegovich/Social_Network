package socialNetwork.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import socialNetwork.db.Comment;


public interface CommentRepo extends JpaRepository<Comment, Long> {
}
