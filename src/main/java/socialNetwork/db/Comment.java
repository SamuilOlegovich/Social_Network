package socialNetwork.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@Table
@Data
@EqualsAndHashCode(of = { "id" })
// класс таблица комм ентариев
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







//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public Message getMessage() {
//        return message;
//    }
//
//    public void setMessage(Message message) {
//        this.message = message;
//    }
//
//    public User getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(User author) {
//        this.author = author;
//    }
}
