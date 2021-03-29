package socialNetwork.dto;


import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import socialNetwork.db.Views;



@Data
@AllArgsConstructor
// чтоб всегдаа класс проскакивал сквозь джексон сериализатор
@JsonView(Views.Id.class)
// класс для расширения универсальности вебСокета (сможем по сокету удалять, добавлять и т д)
public class WsEventDto {
    // характеризует тип объекта
    private ObjectType objectType;
    // характеризует тип события
    private EventType eventType;
    // содержит серелиазованый плоу
    // используется для встраивания джейсона в джейсон при сериализации
    // (так как в этом поле будет лежать строкавормата джейсон)
    @JsonRawValue
    private String body;

//    public WsEventDto() {
//    }







//    public WsEventDto(ObjectType objectType, EventType eventType, String body) {
//        this.objectType = objectType;
//        this.eventType = eventType;
//        this.body = body;
//    }
}
