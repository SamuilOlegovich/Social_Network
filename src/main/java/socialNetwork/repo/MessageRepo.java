package socialNetwork.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import socialNetwork.db.Message;
import socialNetwork.db.User;

import java.util.List;


public interface MessageRepo extends JpaRepository<Message, Long> {
    // возвращает только те меседжи которые принадлежат пользователям из переданного списка
    // (используется чтобы не выдало вначале все сообщения,
    // а потом бегало и искало к каждому отдельно комментарии)
    @EntityGraph(attributePaths = { "comments" })
    Page<Message> findByAuthorIn(List<User> users, Pageable pageable);
}
