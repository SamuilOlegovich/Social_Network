package socialNetwork.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import socialNetwork.db.User;

// JpaRepository<Кто, Айди(тип)>
public interface UserDetailsRepo extends JpaRepository<User, String> {
    // указываем поля которые мы хотим жадно подгружать,
    // то есть к пользователю по айди будут подгружаться подписчики и подписки
    @EntityGraph(attributePaths = { "subscriptions", "subscribers" })
    Optional<User> findById(String s);
}
