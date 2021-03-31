package socialNetwork.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socialNetwork.db.User;
import socialNetwork.db.UserSubscription;
import socialNetwork.repo.UserDetailsRepo;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProfileService {
    private final UserDetailsRepo userDetailsRepo;

    @Autowired
    public ProfileService(UserDetailsRepo userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
    }



    public User changeSubscription(User channel, User subscriber) {
        // берем всех подписчиков данного канала
        List<UserSubscription> subcriptions = channel.getSubscribers()
                // берем Subscribers перегоняем в стрим
                .stream()
                // фильтруем
                .filter(subscription ->
                        subscription.getSubscriber().equals(subscriber))
                // соллектим его в лист
                .collect(Collectors.toList());

        if (subcriptions.isEmpty()) {
            // если пусто добавляем
            UserSubscription subscription = new UserSubscription(channel, subscriber);
            channel.getSubscribers().add(subscription);
        } else {
            // иначе удаляем
            channel.getSubscribers().removeAll(subcriptions);
        }

        return userDetailsRepo.save(channel);
    }
}
