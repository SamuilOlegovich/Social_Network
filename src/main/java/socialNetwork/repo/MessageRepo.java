package socialNetwork.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import socialNetwork.db.Message;


public interface MessageRepo extends JpaRepository<Message, Long> {
    // возвращает сразу к пользователю всю кучу собщений
    // (используется чтобы не выдало вначале все сообщения,
    // а потом бегало и искало к каждому отдельно комментарии)
    @EntityGraph(attributePaths = { "comments" })
    Page<Message> findAll(Pageable pageable);
}
