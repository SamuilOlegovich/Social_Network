package socialNetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import socialNetwork.db.User;
import socialNetwork.db.UserSubscription;
import socialNetwork.repo.UserSubscriptionRepo;

import java.util.List;



public class SubscribersService {
    private final UserSubscriptionRepo userSubscriptionRepo;


    @Autowired
    public SubscribersService(UserSubscriptionRepo userSubscriptionRepo) {
        this.userSubscriptionRepo = userSubscriptionRepo;
    }

    public List<UserSubscription> getSubscribers(User channel) {
        return userSubscriptionRepo.findByChannel(channel);
    }



    // получаем подписку и изменяем ее статус на противоположный
    public UserSubscription changeSubscriptionStatus(User channel, User subscriber) {
        UserSubscription subscription = userSubscriptionRepo.findByChannelAndSubscriber(channel, subscriber);
        subscription.setActive(!subscription.isActive());

        return userSubscriptionRepo.save(subscription);
    }
}
