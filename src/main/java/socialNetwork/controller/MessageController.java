package socialNetwork.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import socialNetwork.db.Message;
import socialNetwork.db.Views;
import socialNetwork.dto.EventType;
import socialNetwork.dto.MetaDto;
import socialNetwork.dto.ObjectType;
import socialNetwork.repo.MessageRepo;
import socialNetwork.util.WsSender;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("messages")
public class MessageController {
    // с помощью этого поля мы выцепляем урл из сообщения
    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    // с помощью этого будем определять что это у нас картинка
    // (определяет что строчка заканчивается на одно из расширений)
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final BiConsumer<EventType, Message> wsSender;
    private final MessageRepo messageRepo;



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
    public Message create(@RequestBody Message message) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        // добавляем в месседж данные о линки прикрепленного контента
        fillMeta(message);
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
    ) throws IOException {
        // спринговый бин который скопирует из меседжа пользователя
        // все поля кроме айди в меседж базы данных
        BeanUtils.copyProperties(message, messageFromDb, "id");
        // добавляем в месседж данные о линки прикрепленного контента
        fillMeta(message);
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


    // заполняет метаданные
    // тут мы дозаполняем месседж - заополняем на основании сообщения
    // сообщение надо разобрать, для этого создаем REGEX
    private void fillMeta(Message message) throws IOException {
        String text = message.getText();
        // из текста получаем матчер
        Matcher matcher = URL_REGEX.matcher(text);

        // проверяем что он находит какое-то значение
        if (matcher.find()) {
            // получаем урл через старт и энд
            String url = text.substring(matcher.start(), matcher.end());

            // проверяем является ли урл картинкой через окончания
            matcher = IMG_REGEX.matcher(url);
            // вставляем линк в сообщение
            message.setLink(url);

            if (matcher.find()) {
                // проверяем картинка ли это и вставляем в setLinkCover
                message.setLinkCover(url);
            } else if (!url.contains("youtu")) {
                // если это не картинка но есть часть слова youtu - получаем метаданные
                MetaDto meta = getMeta(url);

                // полученные данные раскидываем по нужным полям класса
                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
            }
        }
    }

    // получаем и парсим страничку - полученными данными заполняем нужные нам поля
    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first()));
    }

    // проверяем элементы на нулл
    // если нулл возвращаем пустую строку, если нет - получаем атрибут контент
    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
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
