package socialNetwork.db;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@Table
@Data
@EqualsAndHashCode(of = { "id" })
// класс таблица комментариев
public class Comment {
    @Id
    @GeneratedValue
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String text;

    // у одного сообщения может быть много комментов
    // посему отношение коментак к сообщению - множество к одному
    @ManyToOne
    @JoinColumn(name = "message_id")
    // отображает поле только когда запрашиваем полный комментарий
    // работаеи нормально - хорошо
//    @JsonView(Views.FullComment.class)
    // впадет в циклическую зависимость
    // (для избежания ставим доп аноиацию - @JsonIdentityInfo над классом - Message)
    @JsonView(Views.IdName.class)
    private Message message;

    @ManyToOne
    // указываем что поле не может быть нулл и оно не обновляется
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    // отображает всякий раз когда мы запрашиваем наш комент
    @JsonView(Views.IdName.class)
    private User author;

}
