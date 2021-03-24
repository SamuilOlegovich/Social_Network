package socialNetwork.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import socialNetwork.db.Message;
import socialNetwork.db.Views;
import socialNetwork.repo.MessageRepo;

import java.time.LocalDateTime;
import java.util.List;



@RestController
@RequestMapping("messages")
public class MessageController {
    private final MessageRepo messageRepo;


    @Autowired
    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
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
        return messageRepo.save(message);
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
        return messageRepo.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
    }

}
