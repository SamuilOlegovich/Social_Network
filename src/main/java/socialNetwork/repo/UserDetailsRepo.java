package socialNetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import socialNetwork.db.User;

// JpaRepository<Кто, Айди(тип)>
public interface UserDetailsRepo extends JpaRepository<User, String> {
}
