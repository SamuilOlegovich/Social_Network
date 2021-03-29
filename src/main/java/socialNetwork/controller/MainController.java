package socialNetwork.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import socialNetwork.db.User;
import socialNetwork.db.Views;
import socialNetwork.dto.MessagePageDto;
import socialNetwork.repo.MessageRepo;
import socialNetwork.service.MessageService;

import java.util.HashMap;

import static socialNetwork.controller.MessageController.MESSAGES_PER_PAGE;


// просто контроллер не рест, так как будет обрабатывать нашу визуальную часть
@Controller
@RequestMapping("/")
public class MainController {
    private final MessageService messageService;
    // для дев не дев профайла
    @Value("${spring.profiles.active}")
    private String profile;

    private final ObjectWriter writer;


    @Autowired
    public MainController(
            MessageService messageService,
            ObjectMapper objectMapper
    ) {
        this.messageService = messageService;
        this.writer = objectMapper
                .setConfig(objectMapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
    }




    @GetMapping
    public String main(
            Model model,
            @AuthenticationPrincipal User user
    ) throws JsonProcessingException {

        HashMap<Object, Object> data = new HashMap<>();

        if (user != null) {
            data.put("profile", user);

            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0, MESSAGES_PER_PAGE, sort );
            MessagePageDto messagePageDto = messageService.findAll(pageRequest);

            String messages = writer.writeValueAsString(messagePageDto.getMessages());

            model.addAttribute("messages", messages);
            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());
        } else {
            // чтобы не вылетало при деавторизации
            model.addAttribute("messages", "[]");
        }

        model.addAttribute("frontendData", data);
        // проверяет девелоперская это сборка или нет
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }
}
