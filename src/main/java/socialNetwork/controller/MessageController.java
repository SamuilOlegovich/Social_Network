package socialNetwork.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import socialNetwork.db.Message;
import socialNetwork.db.Views;
import socialNetwork.dto.EventType;
import socialNetwork.dto.ObjectType;
import socialNetwork.repo.MessageRepo;
import socialNetwork.util.WsSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;


@RestController
@RequestMapping("messages")
public class MessageController {
    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;


    @Autowired
    public MessageController(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.Id.class);
    }




    @GetMapping
    // показываться будет только айди и имя
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }



    @GetMapping("{id}")
    // показываться будет только айди и время создания
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }



    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        // для работы по сокету
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage);

        return updatedMessage;
    }



    @PutMapping("{id}")
    public Message update(
            // этот месседж найдет в базе по айди переданому в урле
            @PathVariable("id") Message messageFromDb,
            // а это тот месседж что пользователь передал в теле запроса
            @RequestBody Message message
    ) {
        // спринговый бин который скопирует из меседжа пользователя
        // все поля кроме айди в меседж базы данных
        BeanUtils.copyProperties(message, messageFromDb, "id");
        // для работы по сокету
        Message updatedMessage = messageRepo.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        // для работы по сокету
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.REMOVE, updatedMessage);
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
