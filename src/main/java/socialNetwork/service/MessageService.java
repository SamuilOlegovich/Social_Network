package socialNetwork.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import socialNetwork.db.Message;
import socialNetwork.db.User;
import socialNetwork.db.Views;
import socialNetwork.dto.EventType;
import socialNetwork.dto.MessagePageDto;
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



@Service
public class MessageService {
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
    public MessageService(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
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

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }



    // проверяем элементы на нулл
    // если нулл возвращаем пустую строку, если нет - получаем атрибут контент
    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }



    public void delete(Message message) {
        // для работы по сокету
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }



    public Message update(Message messageFromDb, Message message) throws IOException {
        // спринговый бин который скопирует из меседжа пользователя
        // все поля кроме айди в меседж базы данных
        BeanUtils.copyProperties(message, messageFromDb, "id");
        // добавляем в месседж данные о линки прикрепленного контента
        fillMeta(messageFromDb);
        Message updatedMessage = messageRepo.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }



    public Message create(Message message, User user) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        // добавляем в месседж данные о линки прикрепленного контента
        fillMeta(message);
        // устанавливаем автора
        message.setAuthor(user);
        // для работы по сокету
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage);
        return updatedMessage;
    }



    public MessagePageDto findAll(Pageable pageable) {
        Page<Message> page = messageRepo.findAll(pageable);
        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }
}
