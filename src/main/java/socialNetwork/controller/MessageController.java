package socialNetwork.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import socialNetwork.db.Message;
import socialNetwork.db.User;
import socialNetwork.db.Views;
import socialNetwork.dto.MessagePageDto;
import socialNetwork.service.MessageService;

import java.io.IOException;


@RestController
@RequestMapping("messages")
public class MessageController {
    public static final int MESSAGES_PER_PAGE = 3;
    private MessageService messageService;



    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }




    @GetMapping
    // показываться будет только айди и имя
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @AuthenticationPrincipal User user,
            // для просмотра с прокруткой size начальное значение для показа,
            // sort - по какому полю сортируем,
            // direction - в каком направлении сортируем
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"},
                    direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return messageService.findForUser(pageable, user);
    }



    @GetMapping("{id}")
    // показываться будет только айди и время создания
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }



    @PostMapping
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal User user
    ) throws IOException {
        return messageService.create(message, user);
    }



    @PutMapping("{id}")
    public Message update(
            // этот месседж найдет в базе по айди переданому в урле
            @PathVariable("id") Message messageFromDb,
            // а это тот месседж что пользователь передал в теле запроса
            @RequestBody Message message
    ) throws IOException {
        return messageService.update(messageFromDb, message);
    }



    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageService.delete(message);
    }


//    // WebSocket methods ----------------------
//
//    // отвечает за получение сообщений через WebSocket
//    // (клиент шлет сообщения на данный мепин и мы его принимаем)
//    @MessageMapping("/changeMessage")
//    // означает в какой топик (канал в который прилетают сообщения на которые подписываются клиенты)
//    // мы будем складывать ответы
//    @SendTo("/topic/activity")
//    public Message change(Message message) {
//        return messageRepo.save(message);
//    }
}
