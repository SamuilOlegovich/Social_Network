package socialNetwork.util;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Component;
import socialNetwork.dto.EventType;
import socialNetwork.dto.ObjectType;
import socialNetwork.dto.WsEventDto;

import java.util.function.BiConsumer;



@Component
// класс для отправки по сокету всех изменений сделаных через обычные рест методы
public class WsSender {
    // отвечает за отправку сообщений через очереди сообщений в спринге
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;


    public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }


    // для отправки чего либо, не только меседжей но и каких-то объектов которые сейчас еще не определили
    // для этого предусматриваем его широкую реализацию
    // тип события - тип объекта - сам объект - джейсонВью
    // BiConsumer - для замыкания (может принимать один аргумент на входе и ничего не возвращать)
    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view) {
        ObjectWriter writer = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);

        return (EventType eventType, T payload) -> {
            String value = null;

            try {
                value = writer.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            template.convertAndSend(
                    "/topic/activity",
                    new WsEventDto(objectType, eventType, value)
            );
        };
    }
}
