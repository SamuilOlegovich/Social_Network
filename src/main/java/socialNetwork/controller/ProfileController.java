package socialNetwork.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import socialNetwork.service.ProfileService;
import socialNetwork.db.UserSubscription;
import socialNetwork.db.Views;
import socialNetwork.db.User;
import socialNetwork.service.SubscribersService;

import java.util.List;



@RestController
@RequestMapping("profile")
public class ProfileController {
    private final SubscribersService subscribersService;
    private final ProfileService profileService;


    @Autowired
    public ProfileController(ProfileService profileService, SubscribersService subscribersService) {
        this.profileService = profileService;
        this.subscribersService = subscribersService;
    }


    // получить профиль
    @GetMapping("{id}")
    @JsonView(Views.FullProfile.class)
    public User get(@PathVariable("id") User user) {
        return user;
    }



    // изменяем подписку (подписываемся - отписываеимя)
    @PostMapping("change-subscription/{channelId}")
    @JsonView(Views.FullProfile.class)
    public User changeSubscription(
            @AuthenticationPrincipal User subscriber,
            @PathVariable("channelId") User channel
    ) {
        if (subscriber.equals(channel)) {
            // чтобы не подписаться на самого себя
            return channel;
        } else {
            return profileService.changeSubscription(channel, subscriber);
        }
    }



    // возвращает все подписки текущего пользователя
    @GetMapping("get-subscribers/{channelId}")
    @JsonView(Views.IdName.class)
    public List<UserSubscription> subscribers(@PathVariable("channelId") User channel) {
        return subscribersService.getSubscribers(channel);
    }


    // получаем подписку и изменяем ее статус на противоположный
    @PostMapping("change-status/{subscriberId}")
    @JsonView(Views.IdName.class)
    public UserSubscription changeSubscriptionStatus(
            @AuthenticationPrincipal User channel,
            @PathVariable("subscriberId") User subscriber
    ) {
        return subscribersService.changeSubscriptionStatus(channel, subscriber);
    }
}
