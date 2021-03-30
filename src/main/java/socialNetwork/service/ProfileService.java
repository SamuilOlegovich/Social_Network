package socialNetwork.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socialNetwork.db.User;
import socialNetwork.repo.UserDetailsRepo;

import java.util.Set;



@Service
public class ProfileService {
    private final UserDetailsRepo userDetailsRepo;

    @Autowired
    public ProfileService(UserDetailsRepo userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
    }



    public User changeSubscription(User channel, User subscriber) {
        // берем всех подписчиков данного канала
        Set<User> subscribers = channel.getSubscribers();

        if (subscribers.contains(subscriber)) {
            // если такой подписчик есть, значит удаляем его (отписываемся)
            subscribers.remove(subscriber);
        } else {
            subscribers.add(subscriber);
        }

        return userDetailsRepo.save(channel);
    }
}
