package socialNetwork.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import socialNetwork.db.User;
import socialNetwork.db.Views;
import socialNetwork.repo.MessageRepo;
import socialNetwork.repo.UserDetailsRepo;

import java.util.HashMap;


// просто контроллер не рест, так как будет обрабатывать нашу визуальную часть
@Controller
@RequestMapping("/")
public class MainController {
    private final MessageRepo messageRepo;
    // для дев не дев профайла
    @Value("${spring.profiles.active}")
    private String profile;


    @Autowired
    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }




    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) {
        HashMap<Object, Object> data = new HashMap<>();

        data.put("profile", user);
        data.put("messages", messageRepo.findAll());

        model.addAttribute("frontendData", data);
        // проверяет девелоперская это сборка или нет
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }
}
