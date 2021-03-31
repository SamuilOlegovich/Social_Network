package socialNetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import socialNetwork.db.User;
import socialNetwork.db.UserSubscription;
import socialNetwork.db.UserSubscriptionId;

import java.util.List;

public interface UserSubscriptionRepo extends JpaRepository<UserSubscription, UserSubscriptionId> {
    List<UserSubscription> findBySubscriber(User user);
}
