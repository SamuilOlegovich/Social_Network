package socialNetwork.dto;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import socialNetwork.db.Message;
import socialNetwork.db.Views;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class MessagePageDto {
    private List<Message> messages;
    // на каком поле сейчас находимся
    private int currentPage;
    // сколько всего страничек
    private int totalPages;
}
